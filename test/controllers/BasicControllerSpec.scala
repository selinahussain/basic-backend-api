package controllers

import models.Vehicle
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.{when}
import org.scalatest.Matchers._
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Play.materializer
import play.api.http.Status
import play.api.libs.json.{Json}
import play.api.mvc.{ControllerComponents}
import play.api.test.{FakeRequest, Injecting}
import repositories.DataRepository
import play.api.test.Helpers._

import scala.concurrent.{ExecutionContext, Future}

class BasicControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  lazy val controllerComponents: ControllerComponents = app.injector.instanceOf[ControllerComponents]
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val mockDataRepository: DataRepository = mock[DataRepository]

  object testController extends BasicController(
    controllerComponents,
    mockDataRepository,
    executionContext
  )

  val dataModel: Vehicle = Vehicle(
    3,
    true,
    "BMW"
  )

  "BasicController .getOneVehicle" should {

    "return Ok" when {

      "expected vehicle name submitted" in {

        when(mockDataRepository.getVehicle(any[String]))
          .thenReturn(Future(Seq(dataModel)))

        val result = testController.getOneVehicle("BMW")(FakeRequest())

       status(result) mustBe (Status.OK)

      }

    }
  }

  "return NotFound" when {

    "unexpected vehicle name submitted" in {

      when(mockDataRepository.getVehicle(any[String]))
        .thenReturn(Future(Seq()))

      val result = testController.getOneVehicle("aahjhj")(FakeRequest())
      status(result) mustBe (Status.NOT_FOUND)
    }

  }


  "BasicController .receivedForm" should {

    "return Ok" when {
      "expected vehicle name received from POST method" in {
        when(mockDataRepository.getVehicle(any[String]))
          .thenReturn(Future(Seq(dataModel)))
        val result = testController.receiveForm()(FakeRequest())
        status(result) shouldBe (Status.OK)
        contentAsJson(result) shouldBe Json.toJson(dataModel)
      }

      "return NotFound" when {

        "unexpected vehicle name is submitted" in {
          when(mockDataRepository.getVehicle(any[String]))
            .thenReturn(Future(Seq()))
          val result = testController.receiveForm()(FakeRequest())
          status(result) shouldBe (Status.INTERNAL_SERVER_ERROR)
        }
      }

    }

}}
