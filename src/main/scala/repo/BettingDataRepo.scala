package repo

import akka.http.scaladsl.server.Route
import com.datastax.driver.core.{ResultSet, Session}
import constants.QueryConstants._
import constants.model.{MatchDetailsModel, PlaceBet, Teams}

import scala.util.Random

trait BettingDataRepo extends BettingDataMapper {


  def fetchMatchData(session: Session): List[MatchDetailsModel] = {
    val matchResultSet: ResultSet = session.execute(session.prepare(SELECT_MATCHES).bind)

    mapMatchData(matchResultSet)
  }

  def fetchTeamDetail(session: Session, matchId: String): Teams = {
    val stmt =session.prepare(SELECT_MATCH_TEAM).bind("match_id", matchId)

    val resultSet = session.execute(stmt)

    mapTeams(resultSet)
  }

  def placeBet(session:Session, placeBet: PlaceBet): Boolean = {
    val stmtBs = session.prepare(INSERT_BET).bind()
    def uuid = java.util.UUID.randomUUID.toString


    stmtBs.setString("bet_id", uuid)
    stmtBs.setString("session_id", placeBet.sessionId)
    stmtBs.setString("email", placeBet.email)
    stmtBs.setString("kiosk_id", placeBet.kioskId)
    stmtBs.setString("match_id", placeBet.matchId)
    stmtBs.setString("bet_type", placeBet.bet.betType)
    stmtBs.setDouble("amount_placed", placeBet.bet.amount_placed)
    stmtBs.setDouble("amount_due", placeBet.bet.amount_due)

    session.execute(stmtBs).wasApplied
  }

}
