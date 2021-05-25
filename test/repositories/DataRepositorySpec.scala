package repositories

import models.Vehicle
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import play.api.test.Injecting
import uk.gov.hmrc.mongo.MongoComponent

import scala.concurrent.{ExecutionContext, Future}


class DataRepositorySpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  lazy val mongoComponent: MongoComponent = app.injector.instanceOf[MongoComponent]
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]


 object testDataRepository extends DataRepository(mongoComponent)(executionContext)


  "getVehicle() get " should {

    "successfully return vehicle" when {

      "vehicle name matches vehicle options" in {

        val result = testDataRepository.getVehicle("BMW")


        await(result) mustBe List(Vehicle(4, true, "BMW"))

      }
    }


    "unsuccessfully return vehicle" when {

      "vehicle name doesn't match vehicle options" in {

        val result = testDataRepository.getVehicle("test")

        await(result) mustBe List.empty

      }
    }



  }

}
