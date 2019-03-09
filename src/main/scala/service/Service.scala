package service

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.datastax.driver.core.Session
import constants.model._
import repo.BettingDataRepo
import spray.json._
import util.BettingDataUtils


class Service(session: Session) extends BettingDataRepo with BettingDataUtils with RequestJsonSupport {

  val route: Route = cors() {
    path("getMatches") {
      get {
        val (matchIconResponse: List[MatchIconModel], matchDetailsResponse: List[MatchDetailsModel]) = fetchMatchData(session)
        processMasterDataRequest(matchIconResponse, matchDetailsResponse)
      }
    } ~ path("getTeams") {
      get {
        parameter('matchId) {
          matchId: String =>

            val teamDataModel: Teams = fetchTeamDetail(session, matchId)
            processTeamDataRequest(teamDataModel)
        }
      }
    } ~ path("placeBet") {
      post {
        entity(as[PlaceBet]) { placeBetReq: PlaceBet => {
          val response = placeBet(session, placeBetReq)
          processPlaceBetResponse(response)
        }
        }
      }
    } ~ path("getPlacedBet") {
      get {
        parameter('email) { email =>
          val fetchBetResponse: List[PlaceBetModel] = fetchPlacedBet(session, email)
          complete {
            HttpResponse(status = StatusCodes.OK, entity = JsArray(fetchBetResponse.map(x => x.toJson)).compactPrint)
          }
        }
      }
    }
  }

  private def processMasterDataRequest(matchIconResponse: List[MatchIconModel], matchDetails: List[MatchDetailsModel])
  : StandardRoute = complete {
    val entity = processMatchDetails(matchIconResponse, matchDetails)
    HttpResponse(status = StatusCodes.OK,
      entity = JsArray(entity.map(x => x.toJson)).compactPrint
    )
  }

  private def processTeamDataRequest(teamDataModel: Teams): StandardRoute = {
    complete(HttpResponse(status = StatusCodes.OK, entity = teamDataModel.toJson.compactPrint))
  }


  private def processPlaceBetResponse(response: Boolean): StandardRoute = {
    if (response)
      complete(HttpResponse(status = StatusCodes.OK, entity = "Success"))

    else
      complete(HttpResponse(status = StatusCodes.InternalServerError, entity = "Error"))
  }
}

object Service {
  def apply(session: Session): Route = new Service(session).route
}