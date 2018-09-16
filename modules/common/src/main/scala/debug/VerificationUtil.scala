package debug

import errors.ApplicationException
import errors.InternalServerError


object VerificationUtil {
  def verifyWithError[T <: ApplicationException](isValid: Boolean, error: ApplicationException): Unit = {
    if (!isValid) {
      throw error
    }
  }

  def verify(isValid: Boolean, message: String): Unit = verifyWithError(isValid, new InternalServerError(message))
}
