package repo

import java.util

import com.datastax.driver.core.ResultSet
import constants.model.{MatchDetailsModel, Teams}

import scala.collection.JavaConverters._

trait BettingDataMapper {

  def mapMatchData(resultSet: ResultSet): List[MatchDetailsModel] = {
    resultSet.all.asScala.map { row =>
      MatchDetailsModel(row.getString("sports_name"),
        row.getString("match_id"),
        row.getString("match_name"),
        row.getBytes("banner"),
        row.getLong("start_time"),
        row.getLong("end_time")
      )
    }.toList
  }

  def mapTeams(resultSet: ResultSet) = {
    Teams(resultSet.one.getList("teams", classOf[String]).asScala.toList)
  }

}
