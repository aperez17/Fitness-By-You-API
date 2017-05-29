package api.dao

import javax.inject.Inject
import javax.inject.Named
import scala.concurrent.ExecutionContext
import api.model.User
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import com.sksamuel.elastic4s.IndexAndType
import api.model.ElasticModel

class UserDao @Inject()(esModel: ElasticModel, @Named("user") indexAndType: IndexAndType) extends ElasticDao(esModel, indexAndType) {
  
  def indexUser(userEmail: String, user: User) = client execute {
    index into indexAndType source user id userEmail
  }

  def bulkIndex(users: Iterable[User]) = client execute {
    bulk {
      users map (user => index into indexAndType source user)
    }
  }

  def searchByQueryString(q: String)(implicit ec: ExecutionContext) = client execute {
    search in indexAndType query queryStringQuery(q)
  } map (_.as[User])
}