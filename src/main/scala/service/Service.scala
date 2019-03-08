package service

import com.datastax.driver.core.Session

class Service(session: Session) {

  val route =


}

object Service {
  def apply(session: Session): Service = new Service(session)
}