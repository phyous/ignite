package lib

import scala.Predef._
import java.util.{TimeZone, Calendar}

class Heatmap(locations: Seq[Location] = Seq.empty) {
  import Heatmap._

  private[this] val locationMap = locations.foldLeft(Map[Location, Int]() withDefaultValue 0){(h, c) => h.updated(c, h(c)+1)}

  /**
   * Render a 'raw' representation of the meatmap.
   *@return
   */
  def renderRaw = {
    renderHeatmapJson(locationMap, identityNormFunc)
  }

  /**
   * Render a normalized representation of the heatmap subtracting historical trends using model data.
   */
  def renderNormalized = {
    renderHeatmapJson(locationMap, modelNormFunc)
  }
}

object Heatmap {
  def renderHeatmapJson(locationMap: Map[Location, Int], normFunc: (Location, Int) => (Int)):String = {
    def getMax(it:Iterable[Int]):Int = {
      if(it.isEmpty)
        1
      else
        it.max
    }

    val weights = locationMap.map{l => normFunc(l._1, l._2)}
    val maxWeight = getMax(weights)
    val locationWeights = locationMap.zip{weights}

    val locationsString = locationWeights
      .map{l => "{\"lat\":%.4f, \"lng\":%.4f, \"count\": %d}".format(l._1._1.latitude,l._1._1.longitude, l._2)}
      .mkString("[", ",", "]")
    "{\"max\":%d,\"data\":%s}".format(maxWeight, locationsString)
  }

  def identityNormFunc(l: Location, count: Int):Int = count

  def modelNormFunc(l: Location, count:Int):Int = {
    val hour = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.HOUR_OF_DAY)
    val weight = GeoModel.getWeightedLocation(l, count, hour).weight
    (weight * 100).toInt
  }
}