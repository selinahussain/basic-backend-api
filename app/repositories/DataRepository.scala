package repositories

import models.Vehicle


class DataRepository {

  val vehicleOptions = Seq(
    Vehicle(wheels = 4, heavy = true, name = "BMW"),
    Vehicle(wheels = 2, heavy = false, name = "Chopper")
  )

  def getVehicle (vehicleName: String): Option[Vehicle] = vehicleOptions.collectFirst {
    case vehicle if vehicle.name == vehicleName => vehicle
  }
}
