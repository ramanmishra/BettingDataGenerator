package constants

object QueryConstants {

  val SELECT_MATCH_ICON: String = "select sports_name, icon from makeathon.sport_icon"

  val SELECT_MATCHES: String = "select match_id, sports_name, match_name, start_time, end_time, banner from makeathon.matches"

  val SELECT_MATCH_TEAM: String = "select teams from makeathon.matches where match_id= :match_id"

  val INSERT_BET: String = "insert into makeathon.bet(session_id, email, kiosk_id, match_id, bet_type, amount_placed, " +
    "amount_due) VALUES (:session_id, :email, :kiosk_id, :match_id, :bet_type, :amount_placed, :amount_due)"

  val SELECT_BET: String = "select * from makeathon.bet where email= :email"

  val GET_MATCH_NAME: String = "select match_name from makeathon.matches where match_id = :match_id"
}

