package util

import constants.model.{Match, MatchDetails, MatchDetailsModel}

trait BettingDataUtils {

  def processMatchDetails(matchDetails: List[MatchDetailsModel]): List[MatchDetails] = {
    val groupedMatches: Map[String, List[Match]] = matchDetails.groupBy(_.sportName).mapValues {
      value: List[MatchDetailsModel] =>
        value.map(matchDetail =>
          Match(matchDetail.matchId, matchDetail.matchName, matchDetail.startTime, matchDetail.endTime, matchDetail.banner))
    }

    groupedMatches.map((x: (String, List[Match])) => MatchDetails(x._1, x._2)).toList
  }

}
