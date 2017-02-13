package controllers

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import model.{Workout, WorkoutStep, WorkoutDao}
import org.joda.time.DateTime

class WorkoutController @Inject() (workoutDao: WorkoutDao) extends Controller {

  def getHardcoded = Action {
    val workout = Workout(
      workoutId = "workout-1",
      name = "Workout 1",
      imageURL = Some("https://cdn.muscleandstrength.com/sites/all/themes/mnsnew/images/taxonomy/workouts/strength.jpg"),
      steps = List(
        WorkoutStep("Deadlifts", 30),
        WorkoutStep("Squats", 25),
        WorkoutStep("Pushup", 30),
        WorkoutStep("Squat Jumps", 25),
        WorkoutStep("Tricep Dips", 30)),
      time = 30
    )
    Ok(Json.toJson(workout))
  }

  def get(workoutId: String) = Action.async {
    workoutDao.getWorkoutById(workoutId) map {
      case None => NotFound
      case Some(workout) => Ok(Json.toJson(workout))
    }
  }

  def populate() = Action.async {
    /* You can easily convert this endpoint to a bulk insert. Simply parse a `List[Book]` from
       JSON body and pass it instead of `cannedBulkInput` here. */
    workoutDao.bulkIndex(cannedBulkInput) map {
      case resp if !resp.hasFailures => Ok
      case resp => InternalServerError(resp.failures.map(f => f.failureMessage) mkString ";")
    }
  }

  def search(q: String) = Action.async {
    workoutDao.searchByQueryString(q) map {
      case workouts if workouts.length > 0 =>
        Ok(Json.toJson(workouts)).withHeaders("X-Total-Count" -> workouts.length.toString)
      case empty => NoContent
    }
  }
  
  def createWorkout() = Action.async { request =>
    request.body.asJson.map { json =>
      json.asOpt[Workout] match {
        case Some(workout) => workoutDao.bulkIndex(List(workout)) map {
          case resp if !resp.hasFailures => Ok
          case resp => InternalServerError(resp.failures.map(f => f.failureMessage) mkString ";")
        }
        case None => Future.successful(BadRequest("Could not parse Json"))
      }
    } getOrElse { Future.successful(BadRequest("Could not get request body as JSON")) }
  }

  val cannedBulkInput = List(
   Workout(
      workoutId = "workout-1",
      name = "Workout 1",
      imageURL = Some("https://cdn.muscleandstrength.com/sites/all/themes/mnsnew/images/taxonomy/workouts/strength.jpg"),
      steps = List(
        WorkoutStep("Deadlifts", 30),
        WorkoutStep("Squats", 25),
        WorkoutStep("Pushup", 30),
        WorkoutStep("Squat Jumps", 25),
        WorkoutStep("Tricep Dips", 30)),
      time = 30
    ),
   Workout(
      workoutId = "workout-2",
      name = "Workout 2",
      imageURL = Some("https://cdn.muscleandstrength.com/sites/all/themes/mnsnew/images/taxonomy/workouts/strength.jpg"),
      steps = List(
        WorkoutStep("Curls", 10),
        WorkoutStep("Skull Crushers", 20),
        WorkoutStep("Squats", 10),
        WorkoutStep("Deadlifts", 30),
        WorkoutStep("Pullups", 15)),
      time = 40
    ),
    Workout(
      workoutId = "workout-3",
      name = "Killer Abs",
      steps = List(
          WorkoutStep("V-Ups", 50),
          WorkoutStep("Side-to-side V-Ups", 50),
          WorkoutStep("Straddle Ups", 50),
          WorkoutStep("1-leg V-Ups", 50),
          WorkoutStep("Punching Crunchups", 50),
          WorkoutStep("Crunches", 50)),
       time = 12
    ))
}
