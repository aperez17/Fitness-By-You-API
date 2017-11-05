package api.controllers

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import api.model.{ LoginRequest, User}
import api.dao.UserDao
import api.security. { Authenticator, SimpleAuthenticator, Secured }
import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import org.joda.time.DateTime
import api.model.Responses

class UserController @Inject() (resource: UserDao,  auth: SimpleAuthenticator) extends Controller with Secured {
  override val authService: Authenticator = auth

  def populate() = Action.async {
    resource.indexUser("admin", adminUser) map { resp =>
      if(resp.isCreated) {
        Responses.buildOkayResult(Json.toJson(adminUser), Some("Sucessfuly created user"))
      } else {
        Responses.buildInternalServerErrorResult("Could not create User")
      }
    }
  }

  def search(q: String) = Action.async {
    resource.searchByQueryString(q) map {
      case users if users.nonEmpty =>
        Ok(Json.toJson(users)).withHeaders("X-Total-Count" -> users.length.toString)
      case _ => NoContent
    }
  }
  
  def createUser() = Action.async { request =>
    request.body.asJson.map { json =>
      json.asOpt[User] match {
        case Some(user) => resource.indexUser(user.emailAddress, user) map { resp =>
           if(resp.isCreated) {
             Ok(Json.toJson(adminUser))
           } else {
             InternalServerError("Could not create use")
           }
        }
        case None => Future.successful(BadRequest("Could not parse Json"))
      }
    } getOrElse { Future.successful(BadRequest("Could not get request body as JSON")) }
  }
  
  def authenticate() = Action.async { request =>
    auth.authenticate(request)
  }
  
  def logout() = Action.async { request =>
    auth.logout(request)
  }

  val adminUser = User(userName = "admin", emailAddress="admin@example.com")
}
