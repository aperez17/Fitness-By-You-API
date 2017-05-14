package common.localization

import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import scala.io.Source
import common.model.ModelMapping
import common.model.ObjectFieldMapping
import common.model.NestedFieldMapping
import common.model.StringFieldMapping
import common.model.BooleanFieldMapping
import common.model.DateFieldMapping
import common.model.DoubleFieldMapping
import common.model.IntegerFieldMapping
import common.model.FloatFieldMapping

trait Localizer {
  val classz: Class[_]
  val English: String = "en"
  val Spanish: String = "sp"
  val Russian: String = "ru"
  val French: String = "fr"
  
  val language = English
  val models: List[ModelMapping]
  lazy val manager = LocalizerManager(language, classz)
  def localize(path: String): String = {
    manager.localize(path)
  }
  
  def generateStringsForModels = manager.createStringsForModels(models)
}

case class LocalizerManager(language: String, classz: Class[_]) {
  private val localizationFileDir: String = "localization/" + language + ".txt"
  private lazy val localizationMapCache: Map[String, String] = readMapForLanguage
  val uri: java.net.URI = classz.getClassLoader.getResource(localizationFileDir).toURI()
  //TODO there must be a better way to do this
  val confPath: String = classz.getClassLoader.getResource(localizationFileDir).toURI().getPath + "/../../../../../conf/" + localizationFileDir
  val resourcePath: String = classz.getClassLoader.getResource(localizationFileDir).toURI().getPath + "/../../../../../resources/" + localizationFileDir
  val languageFile: File = try {
    new File(confPath)
  } catch {
    case e: FileNotFoundException => new File(resourcePath)
  }
  
  def localize(stringPath: String): String = {
    localizationMapCache.getOrElse(stringPath, stringPath)
  }
  
  private def readMapForLanguage(): Map[String, String] = {
    Source.fromFile(languageFile).getLines()
      .foldLeft(Map.empty[String,String]) { case (map, string) =>
        string.split("=") match {
          case Array(key, value) => map + (key -> value)
          case _ => println(s"Could not properly parse string:\n$string"); map
        }
      }
  }
  
  private def getClientString(name: String): String = {
    name.split("(?=\\p{Upper})").map{ case s =>
      if (s.length > 1) {
        s.substring(0, 1).toUpperCase + s.substring(1, s.length).toLowerCase()
      } else {
        s.toUpperCase()
      }
    }.mkString(" ")
  }
  
  private def getStringsForModel(model: ModelMapping, rootString: String, mappings: Map[String, String]): Map[String, String] = {
    model match {
      case o: ObjectFieldMapping => getStringsForModels(o.mappings, o.modelClass.get.getName + ".", mappings)
      case n: NestedFieldMapping => getStringsForModels(n.mappings, rootString + n.modelName + ".", mappings)
      case s: StringFieldMapping  => mappings + (rootString + s.modelName -> getClientString(s.modelName))
      case i: IntegerFieldMapping  => mappings + (rootString + i.modelName -> getClientString(i.modelName))
      case f: FloatFieldMapping  => mappings + (rootString + f.modelName -> getClientString(f.modelName))
      case d: DoubleFieldMapping  => mappings + (rootString + d.modelName -> getClientString(d.modelName))
      case b: BooleanFieldMapping  => mappings + (rootString + b.modelName -> getClientString(b.modelName))
      case d: DateFieldMapping => mappings + (rootString + d.modelName -> getClientString(d.modelName))
    }
  }
  
  private def getStringsForModels(models: List[ModelMapping], rootString: String, mappings: Map[String, String]): Map[String, String] = {
    models.foldLeft(mappings) { case (map, model) =>
      getStringsForModel(model, rootString, map)
    }
  }
  
  def createStringsForModels(models: List[ModelMapping]) = {
    val mappings = getStringsForModels(models, "", Map.empty[String, String])
    val updatedMappings = mappings.foldLeft(Map.empty[String, String]) { case (map, (key, value)) => 
      map + (key -> localizationMapCache.getOrElse(key, value))
    }
    val printWriter: PrintWriter = new PrintWriter(languageFile)
    println("Writting to : " + languageFile.getPath)
    updatedMappings.foreach{ case (key, value) => printWriter.println(s"$key=$value")}
    printWriter.close()
  }
}