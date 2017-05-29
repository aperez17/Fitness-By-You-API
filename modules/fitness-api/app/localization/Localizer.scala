package api.localization

import common.localization.Localizer
import common.model.ModelMapping
import api.modelRegister.RegisteredModels

case class FitnessLocalizer()

object FitnessLocalizer extends Localizer{
  val classz = classOf[FitnessLocalizer]
  val models: List[ModelMapping] = RegisteredModels.register.values.toList
}