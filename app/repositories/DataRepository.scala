package repositories
import javax.inject.{Inject, Singleton}
import models.Vehicle
import org.mongodb.scala.ReadPreference
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.{IndexModel, IndexOptions}
import org.mongodb.scala.model.Indexes.ascending
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class DataRepository @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[Vehicle](
  collectionName = "vehicles",
  mongoComponent = mongoComponent,
  domainFormat   = Vehicle.format,
  indexes        = Seq(
    IndexModel(ascending("name"), IndexOptions().unique(true))
  ))
{
  // queries and updates can now be implemented with the available `collection: org.mongodb.scala.MongoCollection`
  //def findAll(): Future[Seq[Vehicle]] = collection.withReadPreference(ReadPreference.secondaryPreferred).find().toFuture().map(_.toList)

  def create(vehicle: Vehicle) = collection.insertOne(vehicle).toFuture()

  def getVehicle(vehicleNameFromUrl: String)= collection.find(equal("name", vehicleNameFromUrl)).toFuture()
}

//import javax.inject.Singleton
//import models.Vehicle
//
//@Singleton
//class DataRepository {
//
//  private val vehicle = Seq(
//    Vehicle(2, false, "Chopper"),
//    Vehicle(4, true, "BMW")
//
//  )
//
//  def getVehicle(vehicleNameFromUrl: String): Option[Vehicle] = vehicle.collectFirst {
//    case p if p.name == vehicleNameFromUrl => p
//  }
//
//}