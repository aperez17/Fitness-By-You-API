package api.dao

import javax.inject.Inject
import javax.inject.Named
import scala.concurrent.{ExecutionContext, Future}
import api.model.Workout
import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import api.model.ElasticModel

class WorkoutDao @Inject()(esModel: ElasticModel, @Named("workout") indexAndType: IndexAndType) extends ElasticObjectDao[Workout](esModel, indexAndType)(Workout.WorkoutIndexable, Workout.format, Manifest.classType(classOf[Workout])) {
  
}
