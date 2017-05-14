package api.model

import play.api.libs.json.Json
import play.api.libs.json.JsValue

case class SimpleMessage(message: String)
case class ErrorMessage(message: String, details: Option[String] = None)
case class DataMessage(data: JsValue, message: Option[String] = None)


object SimpleMessage {
  implicit val _ = Json.format[SimpleMessage]
}
object ErrorMessage {
  implicit val _ = Json.format[ErrorMessage]
}
object DataMessage {
  implicit val _ = Json.format[DataMessage]
}