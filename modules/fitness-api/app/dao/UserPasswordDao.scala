package dao

import javax.inject.{Inject, Named}

import api.dao.ElasticObjectDao
import api.model.{ElasticModel, UserPassword}
import com.sksamuel.elastic4s.IndexAndType

/**
  * Created by alexx on 8/12/2018.
  */
class UserPasswordDao @Inject()(esModel: ElasticModel, @Named("userPassword") indexAndType: IndexAndType) extends ElasticObjectDao[UserPassword](esModel, indexAndType)(UserPassword.UserPasswordIndexable, UserPassword.format, Manifest.classType(classOf[UserPassword])){

}
