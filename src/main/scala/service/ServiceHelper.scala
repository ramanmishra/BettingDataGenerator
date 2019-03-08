package service

import scala.concurrent.Future

trait ServiceHelper {
 def fetchData(): Future[Int] ={
   Future.successful(1)
 }
}
