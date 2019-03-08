package service

import com.datastax.driver.core.Session

trait ServiceHelper {

  val session: Session

  def getResponseData(matchId: String): String = {
    "{team:{name:'team1', players:[{name:'player1', run:100, wicket:false/true, distance:100}]}}"
  }
}
