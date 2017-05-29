package api.dao

import javax.inject.Inject
import javax.inject.Named
import scala.concurrent.{ExecutionContext, Future}
import api.model.Workout
import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import api.model.ElasticModel

class WorkoutDao @Inject()(elasticModel: ElasticModel, @Named("workout") indexAndType: IndexAndType) extends ElasticDao(elasticModel, indexAndType) {
  
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
