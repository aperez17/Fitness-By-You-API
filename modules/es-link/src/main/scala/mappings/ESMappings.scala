package esLink.mappings

import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.sksamuel.elastic4s.mappings.TypedFieldDefinition
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers._
import com.sksamuel.elastic4s.ElasticDsl

import api.modelRegister.RegisteredModels
import common.model._

object ESMappings extends ElasticDsl{
  
  def getMappings(): Map[String, MappingDefinition] = {
    RegisteredModels.register.map { case (indexName, modelMap) => indexName -> mappingFromModel(modelMap) }
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
      analyzedDefinition analyzer StopAnalyzer
    } else {
      analyzedDefinition
    }
  }
  
  private def mappingFromModel(modelMapping: ModelMapping) = {
    mapping(modelMapping.modelName).fields(modelMapping.mappings.map(fieldsFromModel): _*)
  }
}