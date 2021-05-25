package controllers

import models.Vehicle
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

  def getOneVehicle(vehicleName: String): Action[AnyContent] = Action.async { implicit request =>

    dataRepository.getVehicle(vehicleName).map(vehicle =>
      Ok(Json.toJson(vehicle.head))) recover {
      case _ => NotFound("Vehicle not found")
    }
  }



  def receiveForm(): Action[AnyContent] = Action.async{implicit request =>
    val jsonReceived =request.body.asJson

    val vehicleNameFromJsonReceived = jsonReceived match {
      case Some(value) => jsonReceived.get.\("Vehicle Name").as[String]
      case None => ""
    }

    dataRepository.getVehicle(vehicleNameFromJsonReceived).map(items => {
      Ok(Json.toJson(items.head))}) recover {
      case _ => InternalServerError(Json.obj(
        "message" -> "error receiving item from mongo"

      ))
    }
  }


  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Vehicle] match {
      case JsSuccess(vehicle, _) =>
        dataRepository.create(vehicle).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

//  def findAll(): Action[AnyContent] = Action.async { implicit request =>
//
//    dataRepository.getVehicle("BMW").map(items => Ok(Json.toJson(items)))
//
//  }

}


