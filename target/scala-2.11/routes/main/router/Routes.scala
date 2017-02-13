
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/cygwin64/home/alexx/Fitness-By-You-API/conf/routes
// @DATE:Sun Feb 12 11:45:28 EST 2017

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:5
  ElasticController_1: javax.inject.Provider[controllers.ElasticController],
  // @LINE:8
  WorkoutController_0: javax.inject.Provider[controllers.WorkoutController],
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:5
    ElasticController_1: javax.inject.Provider[controllers.ElasticController],
    // @LINE:8
    WorkoutController_0: javax.inject.Provider[controllers.WorkoutController]
  ) = this(errorHandler, ElasticController_1, WorkoutController_0, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, ElasticController_1, WorkoutController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """es/stats""", """@controllers.ElasticController@.getStats()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """es/index""", """@controllers.ElasticController@.createIndex()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/workouts/hardcoded""", """@controllers.WorkoutController@.getHardcoded()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/workouts/""" + "$" + """id<[^/]+>""", """@controllers.WorkoutController@.get(id:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/workouts/populate""", """@controllers.WorkoutController@.populate()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/workouts""", """@controllers.WorkoutController@.search(q:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/workouts""", """@controllers.WorkoutController@.createWorkout()"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:5
  private[this] lazy val controllers_ElasticController_getStats0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("es/stats")))
  )
  private[this] lazy val controllers_ElasticController_getStats0_invoker = createInvoker(
    ElasticController_1.get.getStats(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ElasticController",
      "getStats",
      Nil,
      "GET",
      """""",
      this.prefix + """es/stats"""
    )
  )

  // @LINE:6
  private[this] lazy val controllers_ElasticController_createIndex1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("es/index")))
  )
  private[this] lazy val controllers_ElasticController_createIndex1_invoker = createInvoker(
    ElasticController_1.get.createIndex(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ElasticController",
      "createIndex",
      Nil,
      "POST",
      """""",
      this.prefix + """es/index"""
    )
  )

  // @LINE:8
  private[this] lazy val controllers_WorkoutController_getHardcoded2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/workouts/hardcoded")))
  )
  private[this] lazy val controllers_WorkoutController_getHardcoded2_invoker = createInvoker(
    WorkoutController_0.get.getHardcoded(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WorkoutController",
      "getHardcoded",
      Nil,
      "GET",
      """""",
      this.prefix + """api/workouts/hardcoded"""
    )
  )

  // @LINE:9
  private[this] lazy val controllers_WorkoutController_get3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/workouts/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_WorkoutController_get3_invoker = createInvoker(
    WorkoutController_0.get.get(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WorkoutController",
      "get",
      Seq(classOf[String]),
      "GET",
      """""",
      this.prefix + """api/workouts/""" + "$" + """id<[^/]+>"""
    )
  )

  // @LINE:10
  private[this] lazy val controllers_WorkoutController_populate4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/workouts/populate")))
  )
  private[this] lazy val controllers_WorkoutController_populate4_invoker = createInvoker(
    WorkoutController_0.get.populate(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WorkoutController",
      "populate",
      Nil,
      "POST",
      """""",
      this.prefix + """api/workouts/populate"""
    )
  )

  // @LINE:11
  private[this] lazy val controllers_WorkoutController_search5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/workouts")))
  )
  private[this] lazy val controllers_WorkoutController_search5_invoker = createInvoker(
    WorkoutController_0.get.search(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WorkoutController",
      "search",
      Seq(classOf[String]),
      "GET",
      """""",
      this.prefix + """api/workouts"""
    )
  )

  // @LINE:12
  private[this] lazy val controllers_WorkoutController_createWorkout6_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/workouts")))
  )
  private[this] lazy val controllers_WorkoutController_createWorkout6_invoker = createInvoker(
    WorkoutController_0.get.createWorkout(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WorkoutController",
      "createWorkout",
      Nil,
      "POST",
      """""",
      this.prefix + """api/workouts"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:5
    case controllers_ElasticController_getStats0_route(params) =>
      call { 
        controllers_ElasticController_getStats0_invoker.call(ElasticController_1.get.getStats())
      }
  
    // @LINE:6
    case controllers_ElasticController_createIndex1_route(params) =>
      call { 
        controllers_ElasticController_createIndex1_invoker.call(ElasticController_1.get.createIndex())
      }
  
    // @LINE:8
    case controllers_WorkoutController_getHardcoded2_route(params) =>
      call { 
        controllers_WorkoutController_getHardcoded2_invoker.call(WorkoutController_0.get.getHardcoded())
      }
  
    // @LINE:9
    case controllers_WorkoutController_get3_route(params) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_WorkoutController_get3_invoker.call(WorkoutController_0.get.get(id))
      }
  
    // @LINE:10
    case controllers_WorkoutController_populate4_route(params) =>
      call { 
        controllers_WorkoutController_populate4_invoker.call(WorkoutController_0.get.populate())
      }
  
    // @LINE:11
    case controllers_WorkoutController_search5_route(params) =>
      call(params.fromQuery[String]("q", None)) { (q) =>
        controllers_WorkoutController_search5_invoker.call(WorkoutController_0.get.search(q))
      }
  
    // @LINE:12
    case controllers_WorkoutController_createWorkout6_route(params) =>
      call { 
        controllers_WorkoutController_createWorkout6_invoker.call(WorkoutController_0.get.createWorkout())
      }
  }
}
