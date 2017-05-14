package api.dao

import com.evojam.play.elastic4s.PlayElasticJsonSupport
import com.sksamuel.elastic4s.ElasticDsl
import api.model.ElasticModel
import api.localization.FitnessLocalizer

class ElasticDao(elasticModel: ElasticModel)
  extends ElasticDsl with PlayElasticJsonSupport {
 
  lazy val indexAndType = elasticModel.indexAndType
  lazy val client = elasticModel.client
}