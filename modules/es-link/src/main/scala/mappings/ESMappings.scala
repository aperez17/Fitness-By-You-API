package esLink.mappings

import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.sksamuel.elastic4s.mappings.TypedFieldDefinition
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers._
import com.sksamuel.elastic4s.ElasticDsl

object ESMappings extends ElasticDsl{
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
}