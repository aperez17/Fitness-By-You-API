package api.security


import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.sksamuel.elastic4s.ElasticDsl

import api.model.LoginRequest
import api.model.User
import api.model.Responses
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.mvc.Cookie
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Results.BadRequest
import play.api.mvc.Results.Ok
import play.api.mvc.Results.Unauthorized
import play.api.mvc.Security

class SimpleAuthenticator @Inject() (cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends Authenticator with ElasticDsl {
  
  private lazy val client = elasticFactory(cs)
  final val AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
  final val AUTH_TOKEN = "authToken";
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
            val resultWithCookie = Responses.buildOkayResult(Json.toJson(user), Some("Logged in")).withCookies(Cookie(name=AUTH_TOKEN, maxAge=Some(MAX_AGE), value=authToken.get(AUTH_TOKEN).getOrElse(""), secure=true))
            val updatedSession =  resultWithCookie.session(request) + (Security.username, loginRequest.username)
            Future.successful(resultWithCookie.withSession(updatedSession))
          } else {
            Future.successful(Responses.buildUnauthorizedResult("Incorrect UserName or Password"))
          }
        }
        case None => Future.successful(Responses.buildBadRequest(("Incorrect Login Format")))
      }
    } getOrElse { Future.successful(Responses.buildBadRequest("Incorrect JSON Format")) }
  }
  
  def logout(request: Request[AnyContent]): Future[Result] = {
    Future.successful(Responses.buildOkayResult(Json.toJson("Success"), Some("Successfully Logged out")))
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