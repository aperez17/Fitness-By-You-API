package security

import api.model.Responses
import api.model.UserPassword
import api.security.Authenticator
import dao.UserPasswordDao
import debug.VerificationUtil
import errors.InternalServerError
import errors.PasswordMismatchError
import password.PasswordGeneratedResponse
import password.PasswordManager
import play.api.libs.json.Json
import play.api.mvc.AnyContent
import play.api.mvc.Request
import play.api.mvc.Result

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


trait HashAuthenticator extends Authenticator {
  val userPasswordDao: UserPasswordDao

  private def getUserPassword(userId: String): Future[UserPassword] = {
    userPasswordDao.getById(userId).map{
      case Some(userPasswordEntry) => userPasswordEntry
      case _ => throw new InternalServerError(s"Could not locate password for user: $userId")
    }
  }

  private def updatedUserPassword(userId: String, newPassword: String): Future[Result] = {
    val passwordResponse: PasswordGeneratedResponse = PasswordManager.encryptPassword(newPassword)
    val updatedUserPasswordModel: UserPassword = UserPassword(userId, passwordResponse.encryptedPassword, passwordResponse.salt)
    userPasswordDao.indexObjectById(userId, updatedUserPasswordModel).map(indexResult => {
      if (indexResult.getId == userId) {
        Responses.buildOkayResult(Json.toJson("Password was successfully updated"))
      } else {
        Responses.buildInternalServerErrorResult("Could not update password")
      }
    })
  }

  def createPasswordEntryForUser(userId: String, newPassword: String): Future[Result] = {
    updatedUserPassword(userId, newPassword)
  }

  override def resetPassword(userId: String, enteredPassword: String, newPassword: String): Future[Result] = {
    val userPasswordFuture = getUserPassword(userId)
    userPasswordFuture.flatMap(userPassword => {
      val isPasswordValid: Boolean = PasswordManager.isPasswordValid(enteredPassword, userPassword.encryptedPassword, userPassword.saltValue)
      VerificationUtil.verifyWithError(isPasswordValid, new PasswordMismatchError())
      updatedUserPassword(userId, newPassword)
    })
  }

  def authenticate(request: Request[AnyContent]): Future[Result]
}
