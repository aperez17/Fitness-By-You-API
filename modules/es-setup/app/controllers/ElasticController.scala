package es.controllers

import javax.inject.Inject
import play.api.libs.json.Json

import es.model.metadata.ESMappings
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.Logger
import play.api.mvc.{Action, Controller}

import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers._
import com.sksamuel.elastic4s.CreateIndexDefinition
import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.sksamuel.elastic4s.mappings.TypedFieldDefinition

import com.sksamuel.elastic4s.ElasticDsl

import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup


class ElasticController @Inject() (cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends
    Controller with ElasticDsl {

  private val logger = Logger(getClass)
  private lazy val client = elasticFactory(cs)

  def getStats = Action.async {
    try {
      client execute {
        get cluster stats
      } map (response => Ok(response.toString))
    } catch {
      case e: Exception =>
        logger.error("Error connecting to Elasticsearch", e)
        Future(InternalServerError("Error connecting to Elasticsearch. Is application.conf filled in properly?\n"))
    }
  }
  
  val mappings: Map[String, MappingDefinition] = {
    Map(
      ("workouts" -> mapping("workout").fields(
          "workoutId" typed StringType analyzer StopAnalyzer,
          "name" typed StringType,
          "steps" nested (
            "stepName" typed StringType,
            "iterations" typed IntegerType
          ),
          "time" typed IntegerType analyzer StopAnalyzer,
          "imageURL" typed StringType analyzer StopAnalyzer
       )),
       ("users" -> mapping("user").fields(
         "userName" typed StringType analyzer StopAnalyzer,
         "emailAddress" typed StringType analyzer StopAnalyzer,
         "firstName" typed StringType analyzer StopAnalyzer,
         "lastName" typed StringType analyzer StopAnalyzer,
         "currentWeight" typed StringType analyzer StopAnalyzer,
         "lastLoginDate" typed DateType
      ))
    )
  }
  
  def checkIndex(indexName: String): Future[Boolean] = {
    client execute {
      indexExists(indexName)
    } map { resp =>
      resp.isExists()
    }
  }
  
  def putMappings = Action.async {
    try {
      val futureResponses = mappings.toList.map { case (indexName, mapping) =>
        client execute {
          putMapping(indexName / mapping.`type`) as {
            mapping._fields
          }
        }
      }
      Future.sequence(futureResponses).map { responses =>
        Ok(Json.toJson(responses.map(_.toString).mkString(", ")))
      }
    } catch {
      case e: Exception =>
        logger.error("Error updating index", e)
        Future(InternalServerError("Error updating index\n"))
    }
  }
  
  def createIndeces = Action.async {
    try {
     val indexesToCreateMap = mappings.foldLeft(Map.empty[String, Future[Boolean]]) { case (map, (indexName, mapping)) =>
       map + (indexName -> checkIndex(indexName))
     }
     val futureResponses = indexesToCreateMap.map { case (indexName, doesExistFut) =>
       doesExistFut.flatMap { doesExist =>
         if (!doesExist) {
           client execute {
             create index indexName replicas 0 shards 1 mappings(mappings.get(indexName).get)
           } map { resp => resp.toString }
         } else {
           Future.successful(s"Index: $indexName already exists => therefore not created")
         }
       }
     }
     Future.sequence(futureResponses).map (responses =>
       Ok(Json.toJson(responses.mkString(", ")))
     )
    } catch {
      case e: Exception =>
        logger.error("Error creating index", e)
        Future(InternalServerError("Error creating index\n"))
    }
  }
}
