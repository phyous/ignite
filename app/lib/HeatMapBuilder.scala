package lib

import com.twitter.util.{ScheduledThreadPoolTimer, RingBuffer}
import java.util.concurrent.Executors
import com.twitter.logging.Logger
import com.twitter.util.Duration
import twitter4j.conf.Configuration

object HeatmapBuilder {
  private val logger = Logger.get()
}

class HeatmapBuilder(config: Configuration, queueSize: Int, heatmapSize: Int, refreshPeriod: Duration) {

  import HeatmapBuilder._

  private[this] val reader = new TweetStreamReader(config, queueSize)
  private[this] val buffer = new RingBuffer[Location](heatmapSize)

  val thread = Executors.defaultThreadFactory().newThread(new Runnable {
    def run() {
      while (true) {
        val loc = reader.statusQueue.take().getGeoLocation
        if (loc != null && loc.getLatitude != 0 && loc.getLongitude != 0)
          buffer += Location(loc.getLatitude, loc.getLongitude)
      }
    }
  })
  thread.setDaemon(true)
  thread.start()

  @volatile private[this] var heatmap: Heatmap = new Heatmap()

  private[this] val timer = new ScheduledThreadPoolTimer(
    name = "Heatmap-timer",
    makeDaemons = true
  )

  timer.schedule(refreshPeriod, refreshPeriod) {
    logger.ifDebug("Updating heatmap, size %d.".format(buffer.size))
    heatmap = new Heatmap(buffer.toSeq)
  }

  def takeStatus = "%d: %s".format(reader.statusQueue.size(), reader.statusQueue.take())

  def getHeatmap: String = heatmap.renderRaw
  def getNormalizedHeatmap: String = heatmap.renderNormalized
}