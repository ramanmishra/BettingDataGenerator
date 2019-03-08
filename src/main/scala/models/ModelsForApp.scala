package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}


trait AppJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val responseFormat: RootJsonFormat[ResponseData] = jsonFormat2(ResponseData)
}

case class ResponseData(matchName: String, matchData: List[String])


