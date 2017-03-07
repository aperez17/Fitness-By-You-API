package common.model

trait ModelMapping {
  val modelName: String
  val modelType: String
  val isNested: Boolean
  val isAnalyzed: Boolean
  val mappings: List[ModelMapping]
}

case class ObjectFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.objectType,
    isNested: Boolean = false,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping]) extends ModelMapping
    
case class NestedFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.objectType,
    isNested: Boolean = true,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping]) extends ModelMapping

case class StringFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.stringType,
    isNested: Boolean = false,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping

case class DoubleFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.doubleType,
    isNested: Boolean = false,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
    
case class FloatFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.floatType,
    isNested: Boolean = false,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
    
case class BooleanFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.booleanType,
    isNested: Boolean = false,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping
  
case class IntegerFieldMapping(
    modelName: String,
    modelType: String = ModelTypes.integerType,
    isNested: Boolean = false,
    isAnalyzed: Boolean,
    mappings: List[ModelMapping] = List.empty[ModelMapping]) extends ModelMapping