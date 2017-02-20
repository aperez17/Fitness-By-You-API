package model

import play.api.libs.json.Json

import org.joda.time.DateTime

case class WorkoutStep(stepName: String, iterations: Int) {
  require (stepName != null, "step cannot be null")
}

object WorkoutStep {
  implicit val format = Json.format[WorkoutStep]
}

case class Workout(workoutId: String, name: String, imageURL: Option[String] = None, steps: List[WorkoutStep], time: Int) {
  require(workoutId != null, "id cannot be null")
  require(name != null, "name cannot be null")
  require(steps != null, "steps cannot be null")

}

object Workout {
  implicit val format = Json.format[Workout]
}