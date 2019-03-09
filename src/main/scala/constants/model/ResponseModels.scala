package constants.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class MatchDetailsModel(sportName: String,
                             matchId: String,
                             matchName: String,
                             banner: String,
                             startTime: Long,
                             endTime: Long)

case class MatchIconModel(sportsName: String,
                          sportsIcon: String)

case class MatchDetails(sportName: String,
                        icon: String,
                        matches: List[Match])

case class Match(matchId: String,
                 matchName: String,
                 startTime: Long,
                 endTime: Long,
                 banner: String)

case class Teams(details: List[String])

case class BetInfo(teamId: String,
                   amount_placed: Double,
                   betType: String,
                   amount_due: Double)

case class PlaceBet(email: String,
                    sessionId: String,
                    kioskId: String,
                    matchId: String,
                    bet: BetInfo)

case class PlaceBetModel(matchName: String, betType: String, teamId: String, amountPlaced: Double)

trait RequestJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val betInfoFormat: RootJsonFormat[BetInfo] = jsonFormat4(BetInfo)
  implicit val placeBetFormat: RootJsonFormat[PlaceBet] = jsonFormat5(PlaceBet)
  implicit val matchFormat = jsonFormat5(Match)
  implicit val matchDetailsFormat = jsonFormat3(MatchDetails)
  implicit val matchDetailsModelFormat = jsonFormat6(MatchDetailsModel)
  implicit val teamsFormat = jsonFormat1(Teams)
}