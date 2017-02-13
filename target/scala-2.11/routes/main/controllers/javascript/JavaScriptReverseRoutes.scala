
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/cygwin64/home/alexx/Fitness-By-You-API/conf/routes
// @DATE:Sun Feb 12 11:45:28 EST 2017

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:5
  class ReverseElasticController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def getStats: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ElasticController.getStats",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "es/stats"})
        }
      """
    )
  
    // @LINE:6
    def createIndex: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ElasticController.createIndex",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "es/index"})
        }
      """
    )
  
  }

  // @LINE:8
  class ReverseWorkoutController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def search: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WorkoutController.search",
      """
        function(q0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/workouts" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("q", q0)])})
        }
      """
    )
  
    // @LINE:8
    def getHardcoded: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WorkoutController.getHardcoded",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/workouts/hardcoded"})
        }
      """
    )
  
    // @LINE:12
    def createWorkout: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WorkoutController.createWorkout",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/workouts"})
        }
      """
    )
  
    // @LINE:10
    def populate: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WorkoutController.populate",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/workouts/populate"})
        }
      """
    )
  
    // @LINE:9
    def get: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WorkoutController.get",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/workouts/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id0))})
        }
      """
    )
  
  }


}
