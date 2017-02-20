package api.security

import api.model.User
import play.api.mvc.{Action, AnyContent, Controller, Security, Request, RequestHeader, Result, Results }
import play.api.libs.json.Json
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup

trait Secured {
  val authService: Authenticator

  def username(request: RequestHeader): Option[String] = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader): Result = Results.Unauthorized(Json.toJson("Unauthoized please Login to continue"))

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  /**
   * This method shows how you could wrap the withAuth method to also fetch your user
   * You will need to implement UserDAO.findOneByUsername
   */
  def withUser(f: User => Request[AnyContent] => Result) = withAuth { username => implicit request =>
    authService.findOneByUsername(username).map { user =>
      f(user)(request)
    } getOrElse { onUnauthorized(request) }
  }
  
}