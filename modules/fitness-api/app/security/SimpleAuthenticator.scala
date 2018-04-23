package api.security


import javax.inject.Inject

import api.model.LoginRequest
import api.model.Responses
import api.model.User
import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.sksamuel.elastic4s.ElasticDsl
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

class SimpleAuthenticator @Inject() (cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends Authenticator with ElasticDsl {
  
  private lazy val client = elasticFactory(cs)
  final val AUTH_TOKEN_HEADER = "X-AUTH-TOKEN"
  final val AUTH_TOKEN = "authToken"
  final val MAX_AGE = 18000
  
  def authenticate(request: Request[AnyContent]): Future[Result] = {
    request.body.asJson map { json =>
      json.asOpt[LoginRequest] match {
        case Some(loginRequest) =>  {
          if (loginRequest.username == "admin" && loginRequest.password == "password") {
            val authToken = Map(AUTH_TOKEN -> "SOME_ADMIN_AUTH_TOKEN_123")
            val user = findOneByUserEmail(loginRequest.username) match {
              case Some(u) => u
              case _ => return Future.successful(Responses.buildUnauthorizedResult("Incorrect username or password"))
            }
            val resultWithCookie: Result = Responses.buildOkayResult(Json.toJson(user)).withCookies(Cookie(name=AUTH_TOKEN, maxAge=Some(MAX_AGE), value=authToken.get(AUTH_TOKEN).getOrElse(""), secure=true))
            val updatedSession: Session =  resultWithCookie.session(request) + (Security.username, loginRequest.username)
            Future.successful(resultWithCookie.withSession(updatedSession))
          } else {
            Future.successful(Responses.buildUnauthorizedResult("Incorrect UserName or Password"))
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
      findOneByUserEmail(request.session.data(Security.username)) match {
        case Some(u) => Future.successful(Responses.buildOkayResult(Json.toJson(u)))
        case _ => Future.successful(Responses.buildUnauthorizedResult("Incorrect username or password"))
      }
    } else {
      Future.successful(Responses.buildUnauthorizedResult("User is not logged in"))
    }
  }
  
  def logout(request: Request[AnyContent]): Future[Result] = {
    val result = Responses.buildOkayResult(Json.toJson("Success"), Some("Successfully Logged out")).discardingCookies(DiscardingCookie(AUTH_TOKEN))
    val updatedSession: Session = result.session(request) - Security.username
    Future.successful(result.withSession(updatedSession))
  }
  
  def findOneByUserEmail(userEmail: String): Option[User] = {
    val futureResponse =  {
      client execute {
        get id userEmail from "users"/"user"
      }
    }
    // TODO FIND A WAY WITHOUT AWAIT TOO RISKY
    val userString = futureResponse.await(30 seconds).sourceAsString
    Json.parse(userString).asOpt[User]
  }
}