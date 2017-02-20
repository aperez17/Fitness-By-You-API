
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/cygwin64/home/alexx/Fitness-By-You-API/conf/routes
// @DATE:Sun Feb 19 22:45:57 EST 2017

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package controllers {

  // @LINE:5
  class ReverseElasticController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def getStats(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "es/stats")
    }
  
  }

  // @LINE:7
  class ReverseWorkoutController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def search(q:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/workouts" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("q", q)))))
    }
  
    // @LINE:7
    def getHardcoded(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/workouts/hardcoded")
    }
  
    // @LINE:11
    def createWorkout(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/workouts")
    }
  
    // @LINE:9
    def populate(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/workouts/populate")
    }
  
    // @LINE:8
    def get(id:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/workouts/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
    }
  
  }


}
