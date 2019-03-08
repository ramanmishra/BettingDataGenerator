
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import db.DBConfigurations
import http.AbstractActor
import service.BetService

object boot extends AbstractActor with App {
  private val session = new DBConfigurations().getSession

  val routes: Route = BetService(session)

  Http().bindAndHandle(routes, "localhost", 8080)

  println("service started in on : http://localhost:8080" )
}
