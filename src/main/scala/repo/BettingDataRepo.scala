package repo

import akka.http.scaladsl.server.Route
import com.datastax.driver.core.{ResultSet, Session}
import constants.QueryConstants._
import constants.model.{MatchDetailsModel, MatchIconModel, PlaceBet, Teams}

import scala.util.Random

trait BettingDataRepo extends BettingDataMapper {

  def fetchMatchData(session: Session): (List[MatchIconModel], List[MatchDetailsModel]) = {
    val matchResultSet: ResultSet = session.execute(session.prepare(SELECT_MATCHES).bind)
    val matchIconResultSet: ResultSet = session.execute(session.prepare(SELECT_MATCH_ICON).bind)

    val matchIcons: List[MatchIconModel] = mapMatchIcons(matchIconResultSet)
    val matchData: List[MatchDetailsModel] = mapMatchData(matchResultSet)

    (matchIcons, matchData)
  }

  def fetchTeamDetail(session: Session, matchId: String): Teams = {
    val stmt =session.prepare(SELECT_MATCH_TEAM).bind

    stmt.setString("match_id", matchId)

    val resultSet = session.execute(stmt)

    mapTeams(resultSet)
  }

  def placeBet(session:Session, placeBet: PlaceBet): Boolean = {
    val stmtBs = session.prepare(INSERT_BET).bind()
    def uuid = java.util.UUID.randomUUID.toString


    stmtBs.setString("email", placeBet.email)
    stmtBs.setString("bet_id", uuid)
    stmtBs.setString("kiosk_id", placeBet.kioskId)
    stmtBs.setString("match_id", placeBet.matchId)
    stmtBs.setString("session_id", placeBet.sessionId)
    stmtBs.setString("team_id", placeBet.bet.teamId)
    stmtBs.setString("bet_type", placeBet.bet.betType)
    stmtBs.setDouble("amount_placed", placeBet.bet.amount_placed)
    stmtBs.setDouble("amount_due", placeBet.bet.amount_due)

    session.execute(stmtBs).wasApplied
  }

  def fetchPlacedBet(session:Session, email: String) = {
    val stmt = session.prepare(SELECT_BET).bind()

    stmt.setString("email", email)

    val resultSet = session.execute(stmt)

    mapResultSetPlaceBet(resultSet)
  }

}
