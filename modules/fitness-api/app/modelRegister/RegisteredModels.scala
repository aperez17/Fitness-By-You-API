package api.modelRegister

import api.model.{ User, Workout }
import common.model.ModelMapping

object RegisteredModels {
  final val register: Map[String, ModelMapping] = Map(
      "users" -> User.model,
      "workouts" -> Workout.model
    )
}