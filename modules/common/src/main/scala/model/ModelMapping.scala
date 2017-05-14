package common.model

trait ModelMapping {
  val modelName: String
  val modelType: String
  val modelClass: Option[Class[_]]
  val isNested: Boolean
  val isAnalyzed: Boolean
  val mappings: List[ModelMapping]
}

case class ObjectFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.objectType,
    modelClass: Option[Class[_]],
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping]) extends ModelMapping
    
case class NestedFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.objectType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = true,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping]) extends ModelMapping

case class StringFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.stringType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping

case class DoubleFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.doubleType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
    
case class FloatFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.floatType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
    
case class BooleanFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.booleanType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
  
case class IntegerFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.integerType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
    
case class DateFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.dateType,
    modelClass: Option[Class[_]] = None,
    isNested: Boolean = false,
    isAnalyzed: Boolean = false,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping