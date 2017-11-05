package api.dao

import scala.concurrent.ExecutionContext

import play.api.libs.json.OFormat

import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.IndexAndTypes.apply
import com.sksamuel.elastic4s.IndexesAndTypes.apply
import com.sksamuel.elastic4s.source.Indexable

import api.model.ElasticModel
import api.model.User
import javax.inject.Inject
import javax.inject.Named

/**
 * An extensible class for easy methods for indexing, get, update, deletes, search
 */
class ElasticObjectDao[T] (esModel: ElasticModel, indexAndType: IndexAndType)(implicit indexable: Indexable[T], implicit format: OFormat[T])  extends ElasticDao(esModel, indexAndType) {
  
  def indexUser(userEmail: String, user: User) = client execute {
    index into indexAndType source user id userEmail
  }
  
  def indexObjectById(idField: String, obj: T) = client execute {
    index into indexAndType source obj id idField
  }
  
  def indexObject(obj: T) = client execute {
    index into indexAndType source obj
  }
  
  def bulkIndex(objects: Iterable[T]) = {
    objects.map(indexObject)
  }
  
  def bulkIndexById(idField: String, objects: Iterable[T]) = {
    objects.map(indexObjectById(idField, _))
  }

  /**
   * Finds objects by query
   * Note: Use map .as[T] with proper implicits
   */
  def searchByQueryString(q: String) = client execute {
    search in indexAndType query queryStringQuery(q)
  }

  /**
   * Finds objects without a query
   */
  def searchForAll() = client execute {
    search in indexAndType
  }
  
  def getById(idStr: String) = client execute {
    get id idStr from indexAndType
  } map (_.as[T])
  
  def updateObj(obj: T, idStr: String) = client execute {
    update id idStr in indexAndType docAsUpsert obj
  } map (_.as[T])
  
  def deleteObj(idStr: String) = client execute {
    delete id idStr from indexAndType
  }
}