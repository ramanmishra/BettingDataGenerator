package repo

import java.util

import com.datastax.driver.core.ResultSet
import constants.model.{MatchDetailsModel, MatchIconModel, PlaceBetModel, Teams}

import scala.collection.JavaConverters._

trait BettingDataMapper {

  def mapMatchData(resultSet: ResultSet): List[MatchDetailsModel] = {
    resultSet.all.asScala.map { row =>
      val image = row.getBytes("banner")
      MatchDetailsModel(row.getString("sports_name"),
        row.getString("match_id"),
        row.getString("match_name"),
        new String(image.array(), "UTF-8"),
        row.getTimestamp("start_time").getTime,
        row.getTimestamp("end_time").getTime
      )
    }.toList
  }

  def mapMatchIcons(matchIconResultSet: ResultSet): List[MatchIconModel] = {
    matchIconResultSet.all.asScala.map { row =>
      val icon = row.getBytes("icon")
      MatchIconModel(row.getString("sports_name"),
        new String(icon.array(), "UTF-8"),
      )
    }.toList
  }

  def mapTeams(resultSet: ResultSet): Teams = {
    Teams(resultSet.one.getList("teams", classOf[String]).asScala.toList)
  }

  def mapResultSetPlaceBet(resultSet: ResultSet): List[PlaceBetModel] = {
    resultSet.all.asScala.map { row =>
      PlaceBetModel(row.getString("match_id"),
        row.getString("bet_type"),
        row.getString("team_id"), row.getDouble("amount_placed"))
    }.toList
  }

  def mapMatchIdToMatchName(res: List[PlaceBetModel], matchName: String): List[PlaceBetModel] = {
    res.map {
      _.copy(matchId = matchName)
    }
  }
}
