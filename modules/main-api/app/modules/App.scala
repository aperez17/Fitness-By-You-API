package modules

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.mvc.{Action, Controller}

import com.sksamuel.elastic4s.ElasticDsl
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers._

import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup

class ElasticSearchApplication @Inject() (cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends App with ElasticDsl {
  private lazy val client = elasticFactory(cs)
  
  def createIndex(): Future[Boolean] = {
    client execute {
      create index "workouts" replicas 0 shards 1 mappings(
           mapping("workout").fields(
              "workoutId" typed StringType analyzer StopAnalyzer,
              "name" typed StringType,
              "steps" nested (
                "stepName" typed StringType,
                "iterations" typed IntegerType
              ),
              "time" typed IntegerType analyzer StopAnalyzer,
              "imageURL" typed StringType analyzer StopAnalyzer
           )
      )
      create index "users" replicas 0 shards 1 mappings(
          mapping("user").fields(
             "userName" typed StringType analyzer StopAnalyzer,
             "emailAddress" typed StringType analyzer StopAnalyzer,
             "firstName" typed StringType analyzer StopAnalyzer,
             "currentWeight" typed StringType analyzer StopAnalyzer
          )
      )
    } map { response => 
      response.isAcknowledged()
    }
  }
  
  val arguments: Vector[String] = args.toVector
  if (arguments.contains("createIndex")) {
    createIndex() map { wasSuccessful =>
      if (wasSuccessful) {
        println("Successfully created the indexes")
      } else {
        println("ERROR failed to create the indexes")
      }
    }
  }
}