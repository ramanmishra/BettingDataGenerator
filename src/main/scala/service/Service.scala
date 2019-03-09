package service

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.datastax.driver.core.Session
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import constants.model.{MatchDetailsModel, PlaceBet, RequestJsonSupport, Teams}
import repo.BettingDataRepo
import com.google.gson.Gson
import util.BettingDataUtils

class Service(session: Session) extends BettingDataRepo with BettingDataUtils with RequestJsonSupport{
  val gson = new Gson()

  val route = path("getMatches") {
    get {
      val response: List[MatchDetailsModel] = fetchMatchData(session)
      processMasterDataRequest(response)
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

  private def processMasterDataRequest(matchDetails: List[MatchDetailsModel]): StandardRoute = complete {
    HttpResponse(status = StatusCodes.OK, entity = gson.toJson(processMatchDetails(matchDetails)))
  }

  private def processTeamDataRequest(teamDataModel: Teams): StandardRoute = {
    complete(HttpResponse(status = StatusCodes.OK, entity = gson.toJson(teamDataModel)))
  }


  private def processPlaceBetResponse(response: Boolean): StandardRoute = {
    if(response)
      complete(HttpResponse(status = StatusCodes.OK, entity = gson.toJson("Success")))

    else
      complete(HttpResponse(status = StatusCodes.InternalServerError, entity = gson.toJson("Error")))
  }
}

object Service {
  def apply(session: Session): Route = new Service(session).route
}