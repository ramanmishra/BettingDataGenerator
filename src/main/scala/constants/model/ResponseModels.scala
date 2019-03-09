package constants.model

import java.nio.ByteBuffer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class MatchDetailsModel(sportName: String,
                             matchId: String,
                             matchName: String,
                             //                             banner: ByteBuffer,
                             banner: String,
                             startTime: Long,
                             endTime: Long)

case class MatchDetails(sportName: String,
                        matches: List[Match])

case class Match(matchId: String,
                 matchName: String,
                 startTime: Long,
                 endTime: Long,
                 banner: String)

case class Teams(details: List[String])

case class BetInfo(teamId: String,
                   amount_placed: Double,
                   rate: Double,
                   betType: String,
                   amount_due: Double)

case class PlaceBet(email: String,
                    sessionId: String,
                    kioskId: String,
                    matchId: String,
                    bet: BetInfo)

trait RequestJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val betInfoFormat: RootJsonFormat[BetInfo] = jsonFormat5(BetInfo)
  implicit val placeBetFormat: RootJsonFormat[PlaceBet] = jsonFormat5(PlaceBet)
  implicit val matchFormat = jsonFormat5(Match)
  implicit val matchDetailsFormat = jsonFormat2(MatchDetails)
  implicit val matchDetailsModelFormat = jsonFormat6(MatchDetailsModel)

}