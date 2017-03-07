package esLink.mappings

import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.sksamuel.elastic4s.mappings.TypedFieldDefinition
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers._
import com.sksamuel.elastic4s.ElasticDsl

import common.model._

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
  
  private def fieldsFromModel(modelMapping: ModelMapping): TypedFieldDefinition = {
    val modelName = modelMapping.modelName
    val analyzedDefinition = modelMapping.modelType match {
      case ModelTypes.objectType =>
        modelName nested (modelMapping.mappings.map(fieldsFromModel): _*)
      case ModelTypes.stringType =>
        modelName typed StringType
      case ModelTypes.booleanType =>
        modelName typed BooleanType
      case ModelTypes.integerType =>
        modelName typed IntegerType
      case ModelTypes.dateType =>
        modelName typed DateType
      case ModelTypes.floatType =>
        modelName typed FloatType
      case ModelTypes.doubleType =>
        modelName typed DoubleType
    }
    if (modelMapping.isAnalyzed) {
      analyzedDefinition
    } else {
      analyzedDefinition analyzer StopAnalyzer
    }
  }
  
  private def MappingFromModel(modelMapping: ModelMapping) = {
    mapping(modelMapping.modelName).fields(modelMapping.mappings.map(fieldsFromModel): _*)
  }
}