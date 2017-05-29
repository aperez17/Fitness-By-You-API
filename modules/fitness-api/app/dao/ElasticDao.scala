package api.dao

import com.evojam.play.elastic4s.PlayElasticJsonSupport
import com.sksamuel.elastic4s.ElasticDsl
import api.model.ElasticModel
import api.localization.FitnessLocalizer
import com.sksamuel.elastic4s.IndexAndType

class ElasticDao(elasticModel: ElasticModel, indexAndType: IndexAndType)
  extends ElasticDsl with PlayElasticJsonSupport {
  lazy val client = elasticModel.client
}