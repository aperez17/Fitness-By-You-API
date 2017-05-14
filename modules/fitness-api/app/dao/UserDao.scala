package api.dao

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import api.model.User
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import api.model.ElasticModel

class UserDao @Inject()(esModel: ElasticModel) extends ElasticDao(esModel) {
  
  def indexUser(userEmail: String, user: User) = client execute {
    index into indexAndType source user id userEmail
  }

  def bulkIndex(workouts: Iterable[User]) = client execute {
    bulk {
      workouts map (workout => index into indexAndType source workout)
    }
  }

  def searchByQueryString(q: String)(implicit ec: ExecutionContext) = client execute {
    search in indexAndType query queryStringQuery(q)
  } map (_.as[User])
}