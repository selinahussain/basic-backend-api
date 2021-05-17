package repositories

import models.Vehicle
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest


class DataRepositorySpec extends PlaySpec with GuiceOneAppPerTest {

 object testDataRepository extends DataRepository

  "getVehicle() get " should {

    "successfully return vehicle" when {

      "vehicle name matches vehicle options" in {

        val result = testDataRepository.getVehicle("BMW")

        result mustBe Some(Vehicle(wheels = 4, heavy = true, name = "BMW"))

      }
    }

    "unsuccessfully return vehicle" when {

      "vehicle name doesn't match vehicle options" in {

        val result = testDataRepository.getVehicle("test")

        result mustBe None

      }
    }



  }

}
