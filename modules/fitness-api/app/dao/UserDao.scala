package api.dao

import scala.concurrent.ExecutionContext

import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import com.sksamuel.elastic4s.source.Indexable

import api.model.ElasticModel
import api.model.User
import javax.inject.Inject
import javax.inject.Named

class UserDao @Inject()(esModel: ElasticModel, @Named("user") indexAndType: IndexAndType) extends ElasticObjectDao[User](esModel, indexAndType)(User.UserIndexable, User.format){
  
}