package esLink.application

import esLink.link.ESLink
import esLink.mappings.ESMappings
import esLink.link.ESLink

import scala.concurrent.ExecutionContext.Implicits.global

object ESRunner extends App {
  val arguments = args
  println(s"Running with args: ${arguments.mkString(", ")}")
  arguments.map { arg =>
    arg match {
      case "create" => ESLink.createIndeces(ESMappings.getMappings())
      case "upsert" => ESLink.putMappings(ESMappings.getMappings())
      case "stats" => ESLink.getStats()
      case s =>
        println(s"No action for argument $s")
    }
  }
}