package lib

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}
import twitter4j._
import twitter4j.conf.Configuration

case class Twitter4jConfig(name: String)

class TweetStreamReader(config: Configuration, queueSize: Int) {
  val statusQueue: BlockingQueue[Status] = new LinkedBlockingQueue(queueSize);

  def statusListener = new StatusListener() {
    def onStatus(status: Status) {
      statusQueue.add(status)
    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }

  val twitterStream:TwitterStream = new TwitterStreamFactory(config).getInstance()
  twitterStream.addListener(statusListener)
  twitterStream.filter(
    new FilterQuery()
      .locations(Array[Array[Double]](Array[Double](-180,-90),Array[Double](180,90))));

  def cleanup = {
    twitterStream.cleanUp
    twitterStream.shutdown
  }
}