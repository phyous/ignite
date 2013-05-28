package lib

import java.util.Calendar
import java.text.SimpleDateFormat
import play.api.Logger


object SimpleLogger {
  private[this] val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
  @volatile private[this] var requestCount = 0

  def logRequest(request:play.api.mvc.Request[play.api.mvc.AnyContent], extras:String="") {
    val today = Calendar.getInstance().getTime()
    requestCount += 1
    val time = dateFormat.format(today)
    Logger.info("[%d][%s](%s)|%s| %s".format(requestCount, time, request.remoteAddress, request.uri, extras));
  }
}
