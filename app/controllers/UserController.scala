package controllers

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import model.{User, UserDao}
import security. { Authenticator, SimpleAuthenticator, Secured }
import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup
import org.joda.time.DateTime

class UserController @Inject() (userDao: UserDao,  auth: SimpleAuthenticator) extends Controller with Secured {
  override val authService: Authenticator = auth

  def populate() = Action.async {
    /* You can easily convert this endpoint to a bulk insert. Simply parse a `List[Book]` from
       JSON body and pass it instead of `cannedBulkInput` here. */
    userDao.insertUser(adminUser) map { resp =>
      if(resp.isCreated) {
        Ok(Json.toJson(adminUser))
      } else {
        InternalServerError("Could not create use")
      }
    }
  }

  def search(q: String) = Action.async {
    userDao.searchByQueryString(q) map {
      case users if users.length > 0 =>
        Ok(Json.toJson(users)).withHeaders("X-Total-Count" -> users.length.toString)
      case empty => NoContent
    }
  }
  
  def createUser() = Action.async { request =>
    request.body.asJson.map { json =>
      json.asOpt[User] match {
        case Some(user) => userDao.insertUser(user) map { resp =>
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

  val adminUser = User(userName = "admin", emailAddress="admin@example.com")
}
