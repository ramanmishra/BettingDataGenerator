package service

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.datastax.driver.core.Session
import akka.http.scaladsl.server.{Route, StandardRoute}
import constants.model._
import repo.BettingDataRepo
import com.google.gson.Gson
import util.BettingDataUtils
import spray.json._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import akka.http.scaladsl.server.Directives._


class Service(session: Session) extends BettingDataRepo with BettingDataUtils with RequestJsonSupport {
  val gson = new Gson()

  val route = cors() {
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
      complete(HttpResponse(status = StatusCodes.OK, entity = gson.toJson("Success")))

    else
      complete(HttpResponse(status = StatusCodes.InternalServerError, entity = gson.toJson("Error")))
  }
}

object Service {
  def apply(session: Session): Route = new Service(session).route
}