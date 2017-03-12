package api.security


import api.model.{ LoginRequest, User }
import play.api.mvc.{Action, AnyContent, Cookie, Request, RequestHeader,  Result, Security}
import play.api.http._
import play.api.mvc.Results._
import play.api.libs.json.Json
import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

import com.sksamuel.elastic4s.ElasticDsl

import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import play.mvc.Http

class SimpleAuthenticator @Inject() (cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends Authenticator with ElasticDsl {
  
  private lazy val client = elasticFactory(cs)
  final val AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
  final val AUTH_TOKEN = "authToken";
  final val MAX_AGE = 18000
  
  def authenticate(request: Request[AnyContent]): Future[Result] = {
    request.body.asJson map { json =>
      json.asOpt[LoginRequest] match {
        case Some(loginRequest) =>  {
          if (loginRequest.userEmail == "admin" && loginRequest.password == "password") {
            val authToken = Map(AUTH_TOKEN -> "SOME_ADMIN_AUTH_TOKEN_123")
            val resultWithCookie = Ok(Json.toJson(authToken)).withCookies(Cookie(name=AUTH_TOKEN, maxAge=Some(MAX_AGE), value=authToken.get(AUTH_TOKEN).getOrElse(""), secure=true))
            val updatedSession =  resultWithCookie.session(request) + (Security.username, loginRequest.userEmail)
            Future.successful(resultWithCookie.withSession(updatedSession))
          } else {
            Future.successful(Unauthorized("Incorrect UserName or Password"))
          }
        }
        case None => Future.successful(BadRequest("Incorrect Login Format"))
      }
    } getOrElse { Future.successful(BadRequest("Incorrect JSON Format")) }
  }
  
  def findOneByUserEmail(userEmail: String): Option[User] = {
    val futureResponse =  {
      client execute {
        get id userEmail from "users"/"user"
      }
    }
    val userString = futureResponse.await(30 seconds).sourceAsString
    Json.parse(userString).asOpt[User]
  }
}