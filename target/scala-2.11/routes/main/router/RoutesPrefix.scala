
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/cygwin64/home/alexx/Fitness-By-You-API/conf/routes
// @DATE:Sun Feb 19 22:45:57 EST 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
