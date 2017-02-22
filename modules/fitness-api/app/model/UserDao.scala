package api.model

import javax.inject.{Named, Inject}

import scala.concurrent.{ExecutionContext, Future}

import com.sksamuel.elastic4s.{IndexAndType, ElasticDsl}

import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.evojam.play.elastic4s.{PlayElasticFactory, PlayElasticJsonSupport}

class UserDao @Inject()(cs: ClusterSetup, elasticFactory: PlayElasticFactory, @Named("user") indexAndType: IndexAndType)
    extends ElasticDsl with PlayElasticJsonSupport {

  private[this] lazy val client = elasticFactory(cs)
  def insertUser(user: User) = client execute {
    index into indexAndType source user
  }
  
  def indexUser(userId: String, user: User) = client execute {
    index into indexAndType source user id userId
  }
  // original elastic4s .source(doc) expects a DocumentSource or T : Indexable.
  // PlayElasticJsonSupport provides Indexable[T] for any T with Json.Writes[T] available.

  def bulkIndex(workouts: Iterable[User]) = client execute {
    bulk {
      workouts map (workout => index into indexAndType source workout)
    }
  }

  def searchByQueryString(q: String)(implicit ec: ExecutionContext) = client execute {
    search in indexAndType query queryStringQuery(q)
  } map (_.as[User])
  // the .as[T] conversion is available in elastic4s for any T with HitAs[T] instance available.
  // PlayElasticJsonSupport automatically derives HitAs[T] based on Json.Reads[T].
}