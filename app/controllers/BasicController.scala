package controllers

import models.{ Vehicle}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.Results.Ok

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, BaseController, ControllerComponents, Request}
import repositories.DataRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
case class BasicController @Inject()(controllerComponents: ControllerComponents,
                                     dataRepository: DataRepository, ec: ExecutionContext) extends BaseController {

//    def getOneVehicle(vehicleName: String): Action[AnyContent] = Action { implicit request =>
//
//      val vehicle = dataRepository.getVehicle(vehicleName)
//      vehicle match {
//        case Some(Vehicle(wheels,heavy,name)) => Ok(Json.toJson(vehicle.get))
//        case _ =>  NotFound("Vehicle not found")
//      }
//    }

  def receiveForm(): Action[AnyContent] = Action{implicit request =>
    val jsonReceived =request.body.asJson
    val vehicleNameFromJsonReceived = jsonReceived match {
      case Some(value) => jsonReceived.get.\("Vehicle Name").as[String]
      case None => "test"
    }
    val vehicle = dataRepository.getVehicle(vehicleNameFromJsonReceived)
    println("aaa" + vehicle.getClass)

    vehicle match {
      case Vehicle(wheels,heavy,name) => Ok(Json.toJson(vehicle)
      case _ =>  NotFound
    }
  }


  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Vehicle] match {
      case JsSuccess(vehicle, _) =>
        dataRepository.create(vehicle).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def findAll(): Action[AnyContent] = Action.async { implicit request =>

    dataRepository.getVehicle("BMW").map(items => Ok(Json.toJson(items)))

  }

}


