package errors

abstract class UserErrors(message: String) extends ApplicationException(message) {
}

class PasswordMismatchError extends UserErrors("Incorrect User or Password") {
}
