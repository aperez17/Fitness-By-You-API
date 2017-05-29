package esLink.link

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers._
import com.sksamuel.elastic4s.CreateIndexDefinition
import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.sksamuel.elastic4s.mappings.TypedFieldDefinition
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.ElasticDsl._

import com.sksamuel.elastic4s.ElasticDsl


object ESLink extends ElasticDsl {
  //private val client = ElasticClient.transport(ElasticsearchClientUri("127.0.0.1", 9300))
  private val client = ElasticClient.transport(ESSettings.settings, ESSettings.uri)

  def getStats() = {
    try {
      client execute {
        get cluster stats
      } map { response =>
        println("Success: \n",response.toString)
        0
      }
    } catch {
      case e: Exception =>
        Console.err.println("Error connecting to Elasticsearch", e)
        Future.successful(1)
    }
  }
  
  def checkIndex(indexName: String): Future[Boolean] = {
    client execute {
      indexExists(indexName)
    } map { resp =>
      resp.isExists()
    }
  }
  
  def putMappings(mappings: Map[String, MappingDefinition]) = {
    try {
      val futureResponses = mappings.toList.map { case (indexName, mapping) =>
        client execute {
          putMapping(indexName / mapping.`type`) as {
            mapping._fields
          }
        } map { resp => s"Succesffully put mapping for ${mapping.`type`}" }
      }
      Future.sequence(futureResponses).map { responses =>
       responses.foreach(println)
       println("Successfully put mappings")
       0
      }
    } catch {
      case e: Exception =>
        Console.err.println("Error updating index", e)
        Future.successful(1)
    }
  }
  
  def createIndeces(mappings: Map[String, MappingDefinition]) = {
    try {
     val indexesToCreateMap = mappings.foldLeft(Map.empty[String, Future[Boolean]]) { case (map, (indexName, mapping)) =>
       map + (indexName -> checkIndex(indexName))
     }
     val futureResponses = indexesToCreateMap.map { case (indexName, doesExistFut) =>
       doesExistFut.flatMap { doesExist =>
         if (!doesExist) {
           client execute {
             create index indexName replicas 0 shards 1 mappings(mappings.get(indexName).get)
           } map { resp => s"Created index $indexName successfully" }
         } else {
           Future.successful(s"Index: $indexName already exists => therefore not created")
         }
       }
     }
     Future.sequence(futureResponses).map{ responses =>
       responses.foreach(println)
       0
     }
    } catch {
      case e: Exception =>
        Console.err.println("Error creating index", e)
        Future.successful(1)
    }
  }
}
