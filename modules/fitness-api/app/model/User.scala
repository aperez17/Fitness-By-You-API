package api.model

import common.model.DateFieldMapping
import common.model.DoubleFieldMapping
import common.model.ObjectFieldMapping
import common.model.StringFieldMapping
import play.api.libs.json.Json


case class User(
    userName: String,
    emailAddress: String,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    currentWeight: Option[Double] = None,
    profilePicture: Option[String] = None,
    lastLoginDate: Option[java.util.Date] = None) {
}

object User {
  implicit val format = Json.format[User]
  
  val model = 
    ObjectFieldMapping(
      modelName = "user",
      modelClass = Some(classOf[User]),
      isElasticModel = true,
      mappings = List(
          StringFieldMapping(modelName = "userName", isAnalyzed = true, isRequired = true),
          StringFieldMapping(modelName = "emailAddress", isRequired = true),
          StringFieldMapping(modelName = "firstName", isAnalyzed = true),
          StringFieldMapping(modelName = "lastName", isAnalyzed = true),
          StringFieldMapping(modelName = "profilePicture"),
          DoubleFieldMapping(modelName = "currentWeight"),
          DateFieldMapping(modelName = "lastLoginDate")
       )
    )
}

case class LoginRequest(
    username: String,
    password: String){}

object LoginRequest {
  implicit val format = Json.format[LoginRequest]
}

case class UserCreationRequest(
    userEmail: String,
    emailAddress: String,
    password: String,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    currentWeight: Option[Double] = None)
    
object UserCreationRequest {
  implicit val format = Json.format[UserCreationRequest]
  val model = 
    ObjectFieldMapping(
      modelName = "user",
      modelClass = Some(classOf[User]),
      mappings = List(
          StringFieldMapping(modelName = "userName", isAnalyzed = true, isRequired = true),
          StringFieldMapping(modelName = "emailAddress", isRequired = true),
          StringFieldMapping(modelName = "firstName", isAnalyzed = true),
          StringFieldMapping(modelName = "lastName", isAnalyzed = true),
          DoubleFieldMapping(modelName = "currentWeight"),
          DateFieldMapping(modelName = "lastLoginDate")
       )
    )
}