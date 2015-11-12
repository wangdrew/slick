/**
 * Created by andrewwang on 11/5/15.
 */

import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

object MetricsSql extends App {

  val dbName = "andrewwang"
  val postgresHost = "127.0.0.1"
  val postgresUri = s"jdbc:postgresql://${postgresHost}/${dbName}"

  val db = Database.forURL(postgresUri, user="andrewwang", driver = "org.postgresql.Driver")

  val iceblocks = TableQuery[IceBlockData]

  val setup = DBIO.seq(
    // Create the table
    (iceblocks.schema).create,

    // Insert a row
    iceblocks += ("iceblockA", 4.25124)
  )


  // DB operations
  try {
    val setupFuture = db.run(setup)
    println(setupFuture.isCompleted)

    db.run(iceblocks.result).map(_.foreach) {
      case (uuid, powerW) => println(s"${uuid} - ${powerW}")
    }

  } finally db.close
}


class IceBlockData(tag: Tag) extends Table[(String, BigDecimal)](tag, "ICEBLOCKDATA") {
  def uuid = column[String]("uuid")
  def powerW = column[BigDecimal]("powerW")
  def * = (uuid, powerW)
}