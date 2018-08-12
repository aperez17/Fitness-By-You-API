package api.dao

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.OFormat
import com.sksamuel.elastic4s.{IndexAndType, IndexResult}
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import com.sksamuel.elastic4s.source.Indexable
import api.model.ElasticModel
import api.model.User
import javax.inject.Inject
import javax.inject.Named

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * An extensible class for easy methods for indexing, get, update, deletes, search
 */
class ElasticObjectDao[T] (esModel: ElasticModel, indexAndType: IndexAndType)(implicit indexable: Indexable[T], fmt: OFormat[T], m: Manifest[T])  extends ElasticDao(esModel, indexAndType) {
 
  def indexUser(userEmail: String, user: User) = client execute {
    index into indexAndType source user id userEmail
  }
  
  def indexObjectById(idField: String, obj: T): Future[IndexResult] = client execute {
    index into indexAndType source obj id idField
  }
  
  def indexObject(obj: T): Future[IndexResult] = client execute {
    index into indexAndType source obj
  }
  
  def bulkIndex(objects: Iterable[T]): Future[List[IndexResult]] = {
    Future.sequence(objects.map(indexObject)).map(_.toList)
  }
  
  def bulkIndexById(idField: String, objects: Iterable[T]) = {
    objects.map(indexObjectById(idField, _))
  }

  /**
   * Finds objects by query
   * Note: Use map .as[T] with proper implicits
   */
  def searchByQueryString(q: String): Future[List[T]] = client execute {
    search in indexAndType query queryStringQuery(q)
  } map (_.as[T].toList)

  /**
   * Finds objects without a query
   */
  def searchForAll(): Future[List[T]] = client execute {
    search in indexAndType
  } map (_.as[T].toList)
  
  def getById(idStr: String): Future[Option[T]] = client execute {
    get id idStr from indexAndType
  } map (_.as[T])
  
  def updateObj(obj: T, idStr: String) = client execute {
    update id idStr in indexAndType docAsUpsert obj
  }
  
  def deleteObj(idStr: String) = client execute {
    delete id idStr from indexAndType
  }
}