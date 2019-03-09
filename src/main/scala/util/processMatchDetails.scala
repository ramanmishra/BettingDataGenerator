package util

import constants.model.{Match, MatchDetails, MatchDetailsModel, MatchIconModel}

trait BettingDataUtils {

  def processMatchDetails(matchIconResponse: List[MatchIconModel], matchDetails: List[MatchDetailsModel]): List[MatchDetails] = {
    val groupedMatches: Map[String, List[Match]] = matchDetails.groupBy(_.sportName).mapValues {
      value: List[MatchDetailsModel] =>
        value.map(matchDetail =>
          Match(matchDetail.matchId, matchDetail.matchName, matchDetail.startTime, matchDetail.endTime, matchDetail.banner))
    }

    groupedMatches.map { x: (String, List[Match]) =>
      val icon: String = matchIconResponse.filter(_.sportsName == x._1).head.sportsIcon
      MatchDetails(x._1, icon, x._2)
    }.toList
  }

}
