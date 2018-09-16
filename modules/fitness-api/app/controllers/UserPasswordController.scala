package controllers

import javax.inject.Inject

import api.dao.UserDao
import api.model._
import api.security.{Authenticator, Secured, SimpleAuthenticator}
import dao.UserPasswordDao
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class UserPasswordController @Inject() (passwordResource: UserPasswordDao, userResource: UserDao, auth: SimpleAuthenticator) extends Controller with Secured {
  override val authService: Authenticator = auth

  def changePassword = Action.async(request => {
    auth.resetPassword(request)
  })

  def createUser() = Action.async { request =>
    request.body.asJson match {
      case Some(creationRequestAsJson) if creationRequestAsJson.asOpt[UserCreationRequest].nonEmpty => {
        val creationRequest = creationRequestAsJson.as[UserCreationRequest]
        val userModel = creationRequest.toUser
        userResource.indexObjectById(userModel.userName, userModel).flatMap(result => {
          if (result.created) {
            auth.createPasswordEntryForUser(userModel.userName, creationRequest.password).flatMap(result => {
              auth.buildAuthenticationSuccessResult(request, LoginRequest(userModel.userName, ""))
            })
          } else {
            Future.successful(Responses.buildInternalServerErrorResult("Could not create user"))
          }
        })
      }
      case _ => Future.successful(Responses.buildBadRequest("Creation model does not match server"))
    }
  }
}
