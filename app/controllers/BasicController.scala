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



  def getOneVehicle(vehicleName: String) = Action { implicit request =>
    dataRepository.getVehicle(vehicleName) map { vehicle =>
      Ok(Json.toJson(vehicle))
    } getOrElse NotFound

    }

}
