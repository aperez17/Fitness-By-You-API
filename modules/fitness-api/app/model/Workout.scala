package api.model

import play.api.libs.json.Json

import org.joda.time.DateTime
import common.model._

case class WorkoutStep(stepName: String, iterations: Int) {
  require (stepName != null, "step cannot be null")
}

object WorkoutStep {
  implicit val format = Json.format[WorkoutStep]
  
  val model =
    NestedFieldMapping(
      modelName = "steps",
      mappings = List(
          StringFieldMapping(modelName = "stepName", isAnalyzed = true),
          IntegerFieldMapping(modelName = "iterations")
       )
    )
}

case class Workout(workoutId: String, name: String, imageURL: Option[String] = None, steps: List[WorkoutStep], time: Int) {
  require(workoutId != null, "id cannot be null")
  require(name != null, "name cannot be null")
  require(steps != null, "steps cannot be null")

}

object Workout {
  implicit val format = Json.format[Workout]
  
  val model =
    ObjectFieldMapping(
      modelName = "workout",
      modelClass = Some(classOf[Workout]),
      mappings = List(
          StringFieldMapping(modelName = "workoutId", isAnalyzed = true),
          StringFieldMapping(modelName = "name", isAnalyzed = true),
          StringFieldMapping(modelName = "imageURL", isAnalyzed = true),
          WorkoutStep.model,
          IntegerFieldMapping(modelName = "iterations")
       )
    )
}