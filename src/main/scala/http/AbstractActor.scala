package http

import akka.actor. ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.duration.SECONDS

abstract class AbstractActor {
  implicit val config: Config = ConfigFactory.load()
  implicit val actorSystem: ActorSystem = ActorSystem("web-app")
  implicit val materialize: ActorMaterializer = ActorMaterializer()
  implicit val timeout: Timeout = Timeout(config.getInt("akka.askTimeout"), SECONDS)
}
