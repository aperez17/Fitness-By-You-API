package model

import play.api.libs.json.Json


case class User(
    userName: String,
    emailAddress: String,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    currentWeight: Option[String] = None) {
}

object User {
  implicit val format = Json.format[User]
}

case class LoginRequest(
    userName: String,
    password: String){}

object LoginRequest {
  implicit val format = Json.format[LoginRequest]
}