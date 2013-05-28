package controllers

import play.api.mvc._
import com.twitter.conversions.time._
import lib.{SimpleLogger, HeatmapBuilder}
import play.Play

object Heatmap extends Controller {
  var twitter4jConfig = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey(Play.application().configuration().getString("twitter.ConsumerKey"))
    .setOAuthConsumerSecret(Play.application().configuration().getString("twitter.ConsumerSecret"))
    .setOAuthAccessToken(Play.application().configuration().getString("twitter.AccessToken"))
    .setOAuthAccessTokenSecret(Play.application().configuration().getString("twitter.AccessTokenSecret"))
    .build
  val DefaultTwitter4jQueueSize = 100000
  val DefaultHeatmapSize = 4000
  val DefaultRefreshPeriod = 100.millis
  val heatmapBuilder =
    new HeatmapBuilder(twitter4jConfig, DefaultTwitter4jQueueSize, DefaultHeatmapSize, DefaultRefreshPeriod)

  //private[this] val staticData = "{\"max\": 1,\"data\": [{\"lat\": 33.5363, \"lng\":-117.044, \"count\": 1},{\"lat\": 33.5608, \"lng\":-117.24, \"count\": 1}]}"

  def index = Action { implicit request =>
    //val response = staticData
    SimpleLogger.logRequest(request)
    val response = heatmapBuilder.getHeatmap
    Ok(response).as("application/json")
  }

  def normalized = Action { implicit request =>
    //val response = staticData
    SimpleLogger.logRequest(request)
    val response = heatmapBuilder.getNormalizedHeatmap
    Ok(response).as("application/json")
  }

  def status = Action { implicit request =>
    SimpleLogger.logRequest(request)
    val response  = heatmapBuilder.takeStatus
    Ok(response).as("application/json")
  }
}