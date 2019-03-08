import db.DBConfigurations

object boot extends App {
  private val session = new DBConfigurations().getSession


}
