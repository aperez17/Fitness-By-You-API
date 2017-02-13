package model

import javax.inject.{Named, Inject}

import scala.concurrent.{ExecutionContext, Future}

import com.sksamuel.elastic4s.{IndexAndType, ElasticDsl}

import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.evojam.play.elastic4s.{PlayElasticFactory, PlayElasticJsonSupport}

class WorkoutDao @Inject()(cs: ClusterSetup, elasticFactory: PlayElasticFactory, @Named("workout") indexAndType: IndexAndType)
    extends ElasticDsl with PlayElasticJsonSupport {

  private[this] lazy val client = elasticFactory(cs)
  def getWorkoutById(workoutId: String)(implicit ec: ExecutionContext): Future[Option[Workout]] = client execute {
    get id workoutId from indexAndType
  } map (_.as[Workout])
  // the above .as[Book] conversion is available as an extension method
  // provided by PlayElasticJsonSupport
  
  def indexWorkout(workoutId: String, workout: Workout) = client execute {
    index into indexAndType source workout id workoutId
  }
  // original elastic4s .source(doc) expects a DocumentSource or T : Indexable.
  // PlayElasticJsonSupport provides Indexable[T] for any T with Json.Writes[T] available.

  def bulkIndex(workouts: Iterable[Workout]) = client execute {
    bulk {
      workouts map (workout => index into indexAndType source workout)
    }
  }

  def searchByQueryString(q: String)(implicit ec: ExecutionContext) = client execute {
    search in indexAndType query queryStringQuery(q)
  } map (_.as[Workout])
  // the .as[T] conversion is available in elastic4s for any T with HitAs[T] instance available.
  // PlayElasticJsonSupport automatically derives HitAs[T] based on Json.Reads[T].


}
