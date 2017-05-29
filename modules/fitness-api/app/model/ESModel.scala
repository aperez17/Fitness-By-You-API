package api.model

import javax.inject.{Named, Inject}
import com.sksamuel.elastic4s.IndexAndType
import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.evojam.play.elastic4s.PlayElasticFactory

case class ElasticModel @Inject()(
    cs: ClusterSetup,
    elasticFactory: PlayElasticFactory){ 
  lazy val client = elasticFactory(cs)
}