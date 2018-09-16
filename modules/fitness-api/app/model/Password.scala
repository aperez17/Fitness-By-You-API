package api.model

import com.sksamuel.elastic4s.source.Indexable
import common.model.ObjectFieldMapping
import common.model.StringFieldMapping
import password.PasswordManager
import play.api.libs.json.Json

case class UserPassword(
                   userId: String,
                   saltValue: String,
                   encryptedPassword: String
                   ) {
  def doesPasswordMatch(enteredPassword: String): Boolean = {
    PasswordManager.isPasswordValid(enteredPassword, encryptedPassword, saltValue)
  }
}

case class PasswordResetRequest(
                         username: String,
                         newPassword: String,
                         oldPassword: String){}

object PasswordResetRequest {
  implicit val format = Json.format[PasswordResetRequest]
}

object UserPassword {
  implicit val format = Json.format[UserPassword]

  implicit object UserPasswordIndexable extends Indexable[UserPassword] {
    override def json(t: UserPassword): String = Json.toJson(t).toString()
  }

  val model =
    ObjectFieldMapping(
      modelName = "userPassword",
      modelClass = Some(classOf[UserPassword]),
      isElasticModel = true,
      mappings = List(
        StringFieldMapping(modelName = "userName", isAnalyzed = true, isRequired = true),
        StringFieldMapping(modelName = "saltValue", isRequired = true),
        StringFieldMapping(modelName = "encryptedPassword", isRequired = true)
      )
    )
}