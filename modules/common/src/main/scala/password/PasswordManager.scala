package password

import java.io.{BufferedReader, File, FileReader}
import java.nio.file.{Files, Path, Paths}
import java.security.KeyFactory
import java.security.spec.{KeySpec, PKCS8EncodedKeySpec}
import javax.crypto.Cipher

import resources.ResourceManager

import scala.collection.mutable.ArrayBuffer

/**
  * Created by alexx on 8/12/2018.
  */
object PasswordManager {
  lazy private val secretKeyLoc = {
    ResourceManager.runWithFile(getClass, "password/secretKeyLocation.txt", reader => {
      reader.readLine().trim
    })
  }
  lazy private val privateKeyLoc = secretKeyLoc + "/privateKey"
  lazy private val publicKeyLoc = secretKeyLoc + "/publicKey"

  private def getKeySpec(keyLoc: String): KeySpec = {
    val privateKeyFile = new File(keyLoc)
    val path = Paths.get(privateKeyFile.toURI)
    val bytes = Files.readAllBytes(path)
    new PKCS8EncodedKeySpec(bytes)
  }

  def encryptPassword(password: String): String = {
    val keySpec = getKeySpec(privateKeyLoc)
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)
    val encriptor: Cipher = Cipher.getInstance("RSA")
    encriptor.init(Cipher.ENCRYPT_MODE, privateKey)
    encriptor.doFinal(password.getBytes).toString
  }

  // Do not expose the decrypted passwords leave it private
  private def decryptPassword(password: String): String = {
    val keySpec = getKeySpec(publicKeyLoc)
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec)
    val decryptor: Cipher = Cipher.getInstance("RSA")
    decryptor.init(Cipher.DECRYPT_MODE, publicKey)
    decryptor.doFinal(password.getBytes).toString
  }

  def validateEncryptedPasswordWithUserEnteredPassword(enteredPassword: String, encryptedPassword: String): Boolean = {
    decryptPassword(encryptedPassword) == enteredPassword
  }
}
