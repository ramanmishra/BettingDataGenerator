
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import db.DBConfigurations
import http.AbstractActor
import service.Service

object boot extends AbstractActor with App {
  private val session = new DBConfigurations().getSession

  val service: Route = Service(session)

  Http().bindAndHandle(service, "192.168.43.100", 8080)
}
