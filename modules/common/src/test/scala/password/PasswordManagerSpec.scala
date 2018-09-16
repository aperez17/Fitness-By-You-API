package password

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class PasswordManagerSpec extends FlatSpec with Matchers {
  val testPasswordOne: String = "passwordOne"
  val testPasswordTwo: String = "passwordTwo"

  "PasswordManager" should "Encrypt Password with salt" in {
    val passwordGeneratedResponse: PasswordGeneratedResponse = PasswordManager.encryptPassword(testPasswordOne)
    passwordGeneratedResponse.encryptedPassword should not be empty
    passwordGeneratedResponse.salt should not be empty
  }
  it should "Encrypt to same Password with same salt" in {
    val passwordGeneratedResponse: PasswordGeneratedResponse = PasswordManager.encryptPassword(testPasswordOne)
    PasswordManager.isPasswordValid(testPasswordOne, passwordGeneratedResponse.encryptedPassword, passwordGeneratedResponse.salt) should be (true)
  }
  it should "Fail to validate invalid password" in {
    val passwordGeneratedResponse: PasswordGeneratedResponse = PasswordManager.encryptPassword(testPasswordOne)
    PasswordManager.isPasswordValid(testPasswordTwo, passwordGeneratedResponse.encryptedPassword, passwordGeneratedResponse.salt) should be (false)
  }
}
