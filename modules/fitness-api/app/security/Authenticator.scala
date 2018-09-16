package api.security

import api.model.User
import play.api.mvc.Result
import play.api.mvc.AnyContent
import play.api.mvc.Request

import scala.concurrent.Future

trait Authenticator {

  def resetPassword(userId: String, enteredPassword: String, newPassword: String): Future[Result]

  def createPasswordEntryForUser(userId: String, enteredPassword: String): Future[Result]

  def authenticate(request: Request[AnyContent]): Future[Result]
  
  def findOneByUserEmail(userEmail: String): Future[User]
  
  def logout(request: Request[AnyContent]): Future[Result]
}