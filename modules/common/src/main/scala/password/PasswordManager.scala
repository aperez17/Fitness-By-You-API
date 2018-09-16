package password

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

import scala.util.Random


class PasswordGeneratedResponse(final val encryptedPassword: String, final val salt: String)

object PasswordManager {
  final val SaltLength = 16

  private def generateSalt(): Array[Byte] = {
    val bytes = Array.ofDim[Byte](SaltLength)
    Random.nextBytes(bytes)
    bytes
  }

  private def getStringFromEncryptedBytes(encryptedBytes: Array[Byte]): String = {
//    val sb = new StringBuilder
//    encryptedBytes.foreach(byte => {
//      sb.append(Integer.toString((byte & 0xff) + 0x100, 16).substring(1))
//    })
//    sb.toString
    DatatypeConverter.printHexBinary(encryptedBytes)
  }

  private def getEncryptedBytesFromHexString(hexString: String): Array[Byte] = {
    DatatypeConverter.parseHexBinary(hexString)
  }

  // Private because we don't want people encrypting with custom salt values
  private def encryptPasswordWithSalt(password: String, salt: Array[Byte]): Array[Byte] = {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(salt)
    val encryptedPassword = md.digest(password.getBytes())
    encryptedPassword
  }

  def encryptPassword(password: String): PasswordGeneratedResponse = {
    val salt: Array[Byte] = generateSalt()
    val encryptedPassword: Array[Byte] = encryptPasswordWithSalt(password, salt)
    new PasswordGeneratedResponse(getStringFromEncryptedBytes(encryptedPassword), getStringFromEncryptedBytes(salt))
  }

  def isPasswordValid(enteredPassword: String, encryptedPassword: String, salt: String): Boolean = {
    getStringFromEncryptedBytes(encryptPasswordWithSalt(enteredPassword, getEncryptedBytesFromHexString(salt))) == encryptedPassword
  }
}
