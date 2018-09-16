package errors

abstract class InternalExceptions(message: String) extends ApplicationException(message){
}

class InternalServerError(message: String) extends InternalExceptions(message){
  override def getMessage: String = {
    s"An Internal Server error occured: ${super.getMessage}"
  }
}