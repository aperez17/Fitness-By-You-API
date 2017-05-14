package executables.runner

import api.modelRegister.RegisteredModels.register
import api.localization.FitnessLocalizer

object GenerateLocalizations extends App {
  FitnessLocalizer.generateStringsForModels
}