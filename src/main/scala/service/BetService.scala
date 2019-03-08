package service

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.datastax.driver.core.Session

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class Service(session: Session) extends ServiceHelper {

  val route = get {
    path("/") {
      val response: Future[Int] = fetchData()

      response onComplete {
        case Success(x) => complete("Got response")

        case Failure(ex) => ex.printStackTrace()
      }
    }
  }


}

object Service {
  def apply(session: Session): Service = new Service(session).route
}