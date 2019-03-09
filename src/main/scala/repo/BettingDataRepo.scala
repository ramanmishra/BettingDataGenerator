package repo

import com.datastax.driver.core.{ResultSet, Session}
import constants.model._

trait BettingDataRepo extends BettingDataMapper {

  val session: Session
  val ps: PreparedStmts

  def fetchMatchData: (List[MatchIconModel], List[MatchDetailsModel]) = {
    val matchResultSet: ResultSet = session.execute(ps.selectMatches.bind)
    val matchIconResultSet: ResultSet = session.execute(ps.selectMatchIcon.bind)

    val matchIcons: List[MatchIconModel] = mapMatchIcons(matchIconResultSet)
    val matchData: List[MatchDetailsModel] = mapMatchData(matchResultSet)

    (matchIcons, matchData)
  }

  def fetchTeamDetail(matchId: String): Teams = {
    val stmt = ps.selectMatchTeam.bind

    stmt.setString("match_id", matchId)

    val resultSet = session.execute(stmt)

    mapTeams(resultSet)
  }

  def placeBet(placeBet: PlaceBet): Boolean = {
    val stmtBs = ps.insertBet.bind
    def uuid = java.util.UUID.randomUUID.toString

    stmtBs.setString("email", placeBet.email)
    stmtBs.setString("kiosk_id", placeBet.kioskId)
    stmtBs.setString("match_id", placeBet.matchId)
    stmtBs.setString("session_id", placeBet.sessionId)
    stmtBs.setString("team_id", placeBet.bet.teamId)
    stmtBs.setString("bet_type", placeBet.bet.betType)
    stmtBs.setDouble("amount_placed", placeBet.bet.amount_placed)
    stmtBs.setDouble("amount_due", placeBet.bet.amount_due)

    session.execute(stmtBs).wasApplied
  }


  def fetchMatchNameByMatchId(res: List[PlaceBetModel]): List[PlaceBetModel] = {
    val stmt = ps.selectMatchName.bind()

    res.map { matchDetail: PlaceBetModel =>
      val resultSet: ResultSet = session.execute(stmt.setString("match_id", matchDetail.matchId))

      val matchName = resultSet.one().getString("match_name")

      matchDetail.copy(matchId = matchName)
    }
  }

  def fetchPlacedBet(email: String): List[PlaceBetModel] = {
    val stmt = ps.selectBet.bind()

    stmt.setString("email", email)

    val resultSet = session.execute(stmt)

    val res: List[PlaceBetModel] = mapResultSetPlaceBet(resultSet)

    fetchMatchNameByMatchId(res)
  }

  def isSessionValid(sessionId: String) = {
    val stmt = ps.validateSession.bind

    stmt.setString("session_id", sessionId)

    !session.execute(stmt).isExhausted
  }
}
