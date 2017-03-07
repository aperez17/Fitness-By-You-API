package api.modelRegister

import api.model.{ User, Workout }

object RegisteredModels {
  final val register: Map[String, AnyRef] = Map(
      "users" -> User,
      "workouts" -> Workout
    )
}