package repo

import java.util

import com.datastax.driver.core.ResultSet
import constants.model.{MatchDetailsModel, Teams}

import scala.collection.JavaConverters._

trait BettingDataMapper {

  def mapMatchData(resultSet: ResultSet): List[MatchDetailsModel] = {
    val a= resultSet.all.asScala.map { row =>
      val image= row.getBytes("banner")
      MatchDetailsModel(row.getString("sports_name"),
        row.getString("match_id"),
        row.getString("match_name"),
        new String(image.array(), "UTF-8"),
        row.getTimestamp("start_time").getTime,
        row.getTimestamp("end_time").getTime
      )
    }.toList
    a
  }

  def mapTeams(resultSet: ResultSet) = {
    Teams(resultSet.one.getList("teams", classOf[String]).asScala.toList)
  }

}
