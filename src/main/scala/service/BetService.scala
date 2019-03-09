package service

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.Timeout
import com.datastax.driver.core.Session
import com.typesafe.config.Config
import models.AppJsonSupport

import scala.concurrent.duration._

class BetService(DbSession: Session)
                (implicit timeout: Timeout, system: ActorSystem, config: Config)
  extends ServiceHelper with AppJsonSupport {

  val session: Session = DbSession
  val route: Route = path("events") {
    import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._
    get {
      parameter('matchId) { matchId =>
        complete {
          Source
            .tick(5.seconds, 5.seconds, NotUsed)
            .map(_ => getResponseData(matchId))
            .map(ServerSentEvent(_))
            .keepAlive(5.second, () => ServerSentEvent.heartbeat)
        }
      }
    }
  }
}

object BetService {
  def apply(session: Session)(implicit timeout: Timeout, system: ActorSystem,
                              config: Config): Route = new BetService(session).route
}