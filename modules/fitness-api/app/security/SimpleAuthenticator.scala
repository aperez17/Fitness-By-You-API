package api.security


import javax.inject.Inject

import api.model._
import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.sksamuel.elastic4s.ElasticDsl
import dao.UserPasswordDao
import password.PasswordManager
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.mvc.Cookie
import play.api.mvc.DiscardingCookie
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Security
import play.api.mvc.Session

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global

class SimpleAuthenticator @Inject() (userPasswordDao: UserPasswordDao, cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends Authenticator with ElasticDsl {

  private lazy val client = elasticFactory(cs)
  final val AUTH_TOKEN_HEADER = "X-AUTH-TOKEN"
  final val AUTH_TOKEN = "authToken"
  final val MAX_AGE = 18000

  def validatePassword(password: String) = {
    if (password.length < 6) {
      throw new RuntimeException("Password must be atleast 6 characters long")
    }
  }

  def resetPassword(request: Request[AnyContent]): Future[Result] = {
    request.body.asJson.map { json =>
      json.asOpt[PasswordResetRequest] match {
        case Some(resetPasswordRequest) => {
          validatePassword(resetPasswordRequest.newPassword)
          userPasswordDao.getById(resetPasswordRequest.username).flatMap({
            case Some(currentUserPassword) if currentUserPassword.doesPasswordMatch(resetPasswordRequest.oldPassword) => {
              val updatedUserPassword = UserPassword(currentUserPassword.userId, PasswordManager.encryptPassword(resetPasswordRequest.newPassword))
              userPasswordDao.indexObjectById(updatedUserPassword.userId, updatedUserPassword).map(indexResult => {
                if (indexResult.getId == updatedUserPassword.userId) {
                  Responses.buildOkayResult(Json.toJson("Password was successfully reset"))
                } else {
                  Responses.buildInternalServerErrorResult("Could not save new password")
                }
              })
            }
            case _ => Future.successful(Responses.buildUnauthorizedResult("Could not authenticate old password with username"))
          })
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