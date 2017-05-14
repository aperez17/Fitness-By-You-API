package api.model

import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.mvc.Result
import play.api.mvc.Results.BadRequest
import play.api.mvc.Results.Ok
import play.api.mvc.Results.Unauthorized
import play.api.mvc.Results.InternalServerError

object Responses {
  def buildUnauthorizedResult(message: String): Result = {
    Unauthorized(Json.toJson(SimpleMessage(message)))
  }
  def buildOkayResult(data: JsValue, message: Option[String] = None):Result = {
    Ok(Json.toJson(DataMessage(data, message)))
  }
  def buildBadRequest(message: String, jsonOpt: Option[String] = None): Result = {
    BadRequest(Json.toJson(ErrorMessage(message, jsonOpt.map(_.toString))))
  }
  def buildInternalServerErrorResult(message: String, errorOpt: Option[Throwable] = None): Result = {
    InternalServerError(Json.toJson(ErrorMessage(message, errorOpt.map(_.toString()))))
  }
}