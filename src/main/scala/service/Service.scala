package service

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.datastax.driver.core.Session
import constants.model._
import repo.{PreparedStmts, PreparedStmtsInit}
import repo.BettingDataRepo
import spray.json._
import util.BettingDataUtils

class Service(sessionParam: Session) extends BettingDataRepo with BettingDataUtils with RequestJsonSupport with PreparedStmtsInit {
  override val ps: PreparedStmts = prepareStatements(session)
  override val session = sessionParam

  val route: Route = cors() {
    path("getMatches") {
      get {
        parameter('sessionId) {
          sessionId =>
            if (isSessionValid(sessionId)) {
              val (matchIconResponse: List[MatchIconModel], matchDetailsResponse: List[MatchDetailsModel]) = fetchMatchData
              processMasterDataRequest(matchIconResponse, matchDetailsResponse)
            } else
              sendSessionTimeout
        }
      }
    } ~ path("getTeams") {
      get {
        parameter('sessionId, 'matchId) {
          (sessionId: String, matchId: String) =>

            if (isSessionValid(sessionId)) {
              val teamDataModel: Teams = fetchTeamDetail(matchId)
              processTeamDataRequest(teamDataModel)
            } else
              sendSessionTimeout
        }
      }
    } ~ path("placeBet") {
      post {
        entity(as[PlaceBet]) { placeBetReq: PlaceBet => {
          if (isSessionValid(placeBetReq.sessionId)) {
            val response = placeBet(placeBetReq)
            processPlaceBetResponse(response)
          } else
            sendSessionTimeout
        }
        }
      }
    } ~ path("getPlacedBet") {
      get {
        parameter('sessionId, 'email) {
          (sessionId: String, email: String) =>
            if (isSessionValid(sessionId)) {
              val fetchBetResponse = fetchPlacedBet(email)
              complete {
                HttpResponse(status = StatusCodes.OK, entity = "ok")
              }
            } else
              sendSessionTimeout
        }
      }
    }
  }

  private def sendSessionTimeout: StandardRoute = complete {
    HttpResponse(status = StatusCodes.OK,
      entity = ({
        "Timeout"
      })
    )
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
      complete(HttpResponse(status = StatusCodes.OK, entity = {
        "Success"
      })) else
      complete(HttpResponse(status = StatusCodes.InternalServerError, entity = {
        "Error"
      }))
  }
}

object Service {
  def apply(session: Session): Route = new Service(session).route
}