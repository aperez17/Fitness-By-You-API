
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/cygwin64/home/alexx/Fitness-By-You-API/conf/routes
// @DATE:Sun Feb 19 22:45:57 EST 2017

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseElasticController ElasticController = new controllers.ReverseElasticController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseWorkoutController WorkoutController = new controllers.ReverseWorkoutController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseElasticController ElasticController = new controllers.javascript.ReverseElasticController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseWorkoutController WorkoutController = new controllers.javascript.ReverseWorkoutController(RoutesPrefix.byNamePrefix());
  }

}
