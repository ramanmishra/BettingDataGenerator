package constants

object QueryConstants {

  val SELECT_MATCHES: String = "select match_id, sports_name, match_name, start_time, end_time, banner from makeathon.matches"

  val SELECT_MATCH_TEAM: String = "select teams from makeathon.matches where match_id= :match_id"

  val INSERT_BET = "insert into makeathon.bet(bet_id, session_id, email, kiosk_id, match_id, bet_type, amount_placed, " +
    "amount_due) VALUES (:bet_id, :session_id, :email, :kiosk_id, :match_id, :bet_type, :amount_placed, :amount_due)"
}

