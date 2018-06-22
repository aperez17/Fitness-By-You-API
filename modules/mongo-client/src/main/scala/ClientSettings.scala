object ClientSettings {
  /**
    * Provides the implementation of the delegable OAuth1 auth info DAO.
    *
    * @param reactiveMongoApi The ReactiveMongo API.
    * @param config The Play configuration.
    * @return The implementation of the delegable OAuth1 auth info DAO.
    */
  @Provides
  def provideOAuth1InfoDAO(reactiveMongoApi: ReactiveMongoApi, config: Configuration): DelegableAuthInfoDAO[OAuth1Info] = {
    implicit lazy val format = Json.format[OAuth1Info]
    new MongoAuthInfoDAO[OAuth1Info](reactiveMongoApi, config)
  }

  /**
    * Provides the implementation of the delegable OAuth2 auth info DAO.
    *
    * @param reactiveMongoApi The ReactiveMongo API.
    * @param config The Play configuration.
    * @return The implementation of the delegable OAuth2 auth info DAO.
    */
  @Provides
  def provideOAuth2InfoDAO(reactiveMongoApi: ReactiveMongoApi, config: Configuration): DelegableAuthInfoDAO[OAuth2Info] = {
    implicit lazy val format = Json.format[OAuth2Info]
    new MongoAuthInfoDAO[OAuth2Info](reactiveMongoApi, config)
  }

  /**
    * Provides the implementation of the delegable OpenID auth info DAO.
    *
    * @param reactiveMongoApi The ReactiveMongo API.
    * @param config The Play configuration.
    * @return The implementation of the delegable OpenID auth info DAO.
    */
  @Provides
  def provideOpenIDInfoDAO(reactiveMongoApi: ReactiveMongoApi, config: Configuration): DelegableAuthInfoDAO[OpenIDInfo] = {
    implicit lazy val format = Json.format[OpenIDInfo]
    new MongoAuthInfoDAO[OpenIDInfo](reactiveMongoApi, config)
  }

  /**
    * Provides the implementation of the delegable password auth info DAO.
    *
    * @param reactiveMongoApi The ReactiveMongo API.
    * @param config The Play configuration.
    * @return The implementation of the delegable password auth info DAO.
    */
  @Provides
  def providePasswordInfoDAO(reactiveMongoApi: ReactiveMongoApi, config: Configuration): DelegableAuthInfoDAO[PasswordInfo] = {
    implicit lazy val format = Json.format[PasswordInfo]
    new MongoAuthInfoDAO[PasswordInfo](reactiveMongoApi, config)
  }
}