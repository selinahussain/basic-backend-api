package controllers

import models.Vehicle
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import repositories.DataRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
case class BasicController @Inject()(val controllerComponents: ControllerComponents,
                                     dataRepository: DataRepository, implicit val ec: ExecutionContext) extends BaseController {

    def getOneVehicle(vehicleName: String): Action[AnyContent] = Action { implicit request =>

      val vehicle = dataRepository.getVehicle(vehicleName)
      vehicle match {
        case Some(Vehicle(wheels,heavy,name)) => Ok(Json.toJson(vehicle.get))
        case _ =>  NotFound("Vehicle not found")
      }
    }

  def receiveForm(): Action[AnyContent] = Action {implicit request =>
    val jsonReceived =request.body.asJson.get
    val name = (jsonReceived \ "Vehicle Name").as[String]
    val vehicle = dataRepository.getVehicle(name)

    vehicle match {
      case Some(Vehicle(wheels,heavy,name)) => Ok(Json.toJson(vehicle.get))
      case _ =>  NotFound("Vehicle not found")
    }
  }

}


