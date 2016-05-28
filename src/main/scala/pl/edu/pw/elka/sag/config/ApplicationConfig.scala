package pl.edu.pw.elka.sag.config

import akka.util.Timeout

import scala.concurrent.duration._

object ApplicationConfig {
  val finiteDuration: FiniteDuration = 300 seconds
  implicit val timeout = Timeout(finiteDuration)

  val actorSize = 10000

}
