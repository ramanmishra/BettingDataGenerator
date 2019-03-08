package service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import com.datastax.driver.core.Session
import com.typesafe.config.Config

class BetService(session: Session)
                (implicit timeout: Timeout, system: ActorSystem, config: Config) extends ServiceHelper {

  val route: Route = get {
    complete(
      HttpEntity(
        ContentTypes.`text/plain(UTF-8)`, "message"
      )
    )
  }
}

object BetService {
  def apply(session: Session)(implicit timeout: Timeout, system: ActorSystem,
                              config: Config): Route = new BetService(session).route
}