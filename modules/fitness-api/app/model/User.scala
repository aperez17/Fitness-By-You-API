package api.model

import play.api.libs.json.Json
import common.model._


case class User(
    userName: String,
    emailAddress: String,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    currentWeight: Option[Double] = None,
    lastLoginDate: Option[java.util.Date] = None) {
}

object User {
  implicit val format = Json.format[User]
  
  val model = 
    ObjectFieldMapping(
      modelName = "user",
      mappings = List(
          StringFieldMapping(modelName = "userName", isAnalyzed = true),
          StringFieldMapping(modelName = "emailAddress"),
          StringFieldMapping(modelName = "firstName"),
          StringFieldMapping(modelName = "lastName"),
          DoubleFieldMapping(modelName = "currentWeight"),
          DateFieldMapping(modelName = "lastLoginDate")
       )
    )
}

case class LoginRequest(
    userName: String,
    password: String){}

object LoginRequest {
  implicit val format = Json.format[LoginRequest]
}