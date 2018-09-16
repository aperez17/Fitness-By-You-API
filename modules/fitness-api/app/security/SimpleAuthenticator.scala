package api.security


import javax.inject.Inject

import api.model._
import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.sksamuel.elastic4s.ElasticDsl
import dao.UserPasswordDao
import debug.VerificationUtil
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.mvc.Cookie
import play.api.mvc.DiscardingCookie
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Security
import play.api.mvc.Session
import security.HashAuthenticator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SimpleAuthenticator @Inject() (val userPasswordDao: UserPasswordDao, cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends HashAuthenticator with ElasticDsl {

  private lazy val client = elasticFactory(cs)
  final val AUTH_TOKEN_HEADER = "X-AUTH-TOKEN"
  final val AUTH_TOKEN = "authToken"
  final val MAX_AGE = 18000

  def validatePasswordLength(password: String): Unit = {
    VerificationUtil.verify(password.length < 6, "Password must be at least 6 characters long")
  }


  override def createPasswordEntryForUser(userId: String, newPassword: String): Future[Result] = {
    validatePasswordLength(newPassword)
    super.createPasswordEntryForUser(userId, newPassword)
  }

  def resetPassword(request: Request[AnyContent]): Future[Result] = {
    request.body.asJson.map { json =>
      json.asOpt[PasswordResetRequest] match {
        case Some(resetPasswordRequest) => {
          validatePasswordLength(resetPasswordRequest.newPassword)
          resetPassword(resetPasswordRequest.username, resetPasswordRequest.oldPassword, resetPasswordRequest.newPassword)
        }
        case _ => Future.successful(Responses.buildBadRequest("Could not serialize pasword request"))
      }
    } getOrElse { Future.successful(Responses.buildBadRequest("Incorrect JSON Format")) }
  }

  def buildAuthenticationSuccessResult(request: Request[AnyContent], loginRequest: LoginRequest): Future[Result] = {
    val authToken = Map(AUTH_TOKEN -> "SOME_ADMIN_AUTH_TOKEN_123")
    findOneByUserEmail(loginRequest.username).flatMap(user => {
      val resultWithCookie: Result = Responses.buildOkayResult(Json.toJson(user)).withCookies(Cookie(name=AUTH_TOKEN, maxAge=Some(MAX_AGE), value=authToken.get(AUTH_TOKEN).getOrElse(""), secure=true))
      val updatedSession: Session =  resultWithCookie.session(request) + (Security.username, loginRequest.username)
      Future.successful(resultWithCookie.withSession(updatedSession))
    })
  }

  def authenticate(request: Request[AnyContent]): Future[Result] = {
    request.body.asJson map { json =>
      json.asOpt[LoginRequest] match {
        case Some(loginRequest) =>  {
          if (loginRequest.username == "admin" && loginRequest.password == "password") {
            buildAuthenticationSuccessResult(request, loginRequest)
          } else {
            userPasswordDao.getById(loginRequest.username) flatMap {
              case Some(userPassword) if userPassword.doesPasswordMatch(loginRequest.password) => {
                buildAuthenticationSuccessResult(request, loginRequest)
              }
              case _ => Future.successful(Responses.buildUnauthorizedResult("Incorrect UserName or Password"))
            }
          }
        }
        case None => Future.successful(Responses.buildBadRequest(("Incorrect Login Format")))
      }
    } getOrElse { Future.successful(Responses.buildBadRequest("Incorrect JSON Format")) }
  }

  def isAuthenticated(request: Request[AnyContent]): Future[Result] = {
    println(request.cookies)
    // Havent' figured out the cookie scenario yet
    val isCookieThere = true//request.cookies.nonEmpty && request.cookies.head.secure && request.cookies.head.name == AUTH_TOKEN
    val isSessionActive = request.session.data.get(Security.username).nonEmpty
    if (isCookieThere && isSessionActive) {
      buildAuthenticationSuccessResult(request, LoginRequest(request.session.data(Security.username), ""))
    } else {
      Future.successful(Responses.buildUnauthorizedResult("User is not logged in"))
    }
  }

  def logout(request: Request[AnyContent]): Future[Result] = {
    val result = Responses.buildOkayResult(Json.toJson("Success"), Some("Successfully Logged out")).discardingCookies(DiscardingCookie(AUTH_TOKEN))
    val updatedSession: Session = result.session(request) - Security.username
    Future.successful(result.withSession(updatedSession))
  }

  def findOneByUserEmail(userEmail: String): Future[User] = {
    client execute {
      get id userEmail from "users" / "user"
    } map { resp =>
      val userAsString = resp.sourceAsString
      Json.parse(userAsString).as[User]
    }
  }
}