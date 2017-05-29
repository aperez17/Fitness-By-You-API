package api.model

import play.api.libs.json.Json

import org.joda.time.DateTime
import common.model._

case class WorkoutStep(step: String, iterations: Int, time: Int) {
}

object WorkoutStep {
  implicit val format = Json.format[WorkoutStep]
  
  val model =
    NestedFieldMapping(
      modelName = "steps",
      mappings = List(
          StringFieldMapping(modelName = "step", isRequired = true),
          IntegerFieldMapping(modelName = "iterations", isRequired = true),
          IntegerFieldMapping(modelName = "time", isRequired = true)
       )
    )
}

case class Workout(workoutId: String,
    name: String,
    imageURL: Option[String] = None,
    steps: List[WorkoutStep],
    totalTime: Int) {
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
      isElasticModel = true,
      mappings = List(
          StringFieldMapping(modelName = "workoutId", isAnalyzed = true, isRequired = true, isDisplayed = false),
          StringFieldMapping(modelName = "name", isAnalyzed = true, isRequired = true),
          StringFieldMapping(modelName = "workoutImage", isAnalyzed = true),
          WorkoutStep.model,
          IntegerFieldMapping(modelName = "iterations", isRequired = true),
          IntegerFieldMapping(modelName = "totalTime", isRequired = true)
       )
    )
}