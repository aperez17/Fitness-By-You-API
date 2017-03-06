package esLink.link

import org.elasticsearch.common.settings.Settings
import com.sksamuel.elastic4s.ElasticsearchClientUri

object ESSettings {
  val settings = Settings.builder().put("cluster.name", "fitness-by-you").build()
  val uri = ElasticsearchClientUri("elasticsearch://127.0.0.1:9300")
}