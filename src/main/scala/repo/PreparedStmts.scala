package repo

import com.datastax.driver.core.{PreparedStatement, Session}
import constants.QueryConstants._

case class PreparedStmts(validateSession: PreparedStatement,
                         selectMatchIcon: PreparedStatement,
                         selectMatches: PreparedStatement,
                         selectMatchTeam: PreparedStatement,
                         insertBet: PreparedStatement,
                         selectBet: PreparedStatement)

trait PreparedStmtsInit {
  def prepareStatements(session: Session): PreparedStmts = {
    PreparedStmts(
      session.prepare(VALIDATE_SESSION),
      session.prepare(SELECT_MATCH_ICON),
      session.prepare(SELECT_MATCHES),
      session.prepare(SELECT_MATCH_TEAM),
      session.prepare(INSERT_BET),
      session.prepare(SELECT_BET))
  }
}