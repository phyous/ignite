package lib

import play.api.Play
import play.api.Play.current
import java.util.{TimeZone, Calendar}

case class GeoModelHourly(geoSet: Map[Location, Double], scalingFactor: Double)

case class WeightedLocation(location: Location, weight: Float)

object GeoModel extends GeoCalculation {
  private[this] val Hours = 24
  private[this] val ModelGranularity = 0.1
  @volatile private[this] var scalingFactor = 1.0

  private[this] lazy val models: Map[Int, GeoModelHourly] = {
    (0 to Hours - 1).zip((0 to Hours - 1).map {
      x => loadModel(x)
    }).toMap
  }

  private[this] def loadModel(hour: Int): GeoModelHourly = {
    val inputStream = Play.classloader.getResourceAsStream("resources/%d.model".format(hour))
    val pointIterator = scala.io.Source.fromInputStream(inputStream).getLines()
    // First line of model file is scaling factor
    scalingFactor = pointIterator.next.toFloat
    // Remaining lines are point vectors
    val pointSet = pointIterator.map {
      line =>
        val point = line.split(",")
        (Location(point(0).toDouble, point(1).toDouble), point(2).toDouble)
    }.toMap
    GeoModelHourly(pointSet, scalingFactor)
  }

  private[this] def getPointWeight(location: Location, hour: Int) = (models(hour).geoSet).getOrElse(location, scalingFactor)

  def getWeightedLocation(loc: Location, count: Int, hour: Int): WeightedLocation = {
    def shiftPoint(pos:Double, magnitude:Int): Double =
      Math.floor((pos + magnitude*ModelGranularity)*10)/10

    def computeWeightFactor = {
      val neighbors = Seq(
        Location(shiftPoint(loc.latitude, 1), shiftPoint(loc.longitude, 1)),
        Location(shiftPoint(loc.latitude, -1), shiftPoint(loc.longitude, -1)),
        Location(shiftPoint(loc.latitude, -1), shiftPoint(loc.longitude, 1)),
        Location(shiftPoint(loc.latitude, 1), shiftPoint(loc.longitude, -1)))
      val modelNeighbors = neighbors.map{getPointWeight(_, hour)}

      val pointDistance = modelNeighbors.zip{neighbors.map{haversineDistance(loc, _)}}
      val totalDistance = pointDistance.map{_._2}.sum

      val scalingFactor = pointDistance.map{x => x._2 * x._1}.sum / totalDistance
      val normalizedCount = (Math.log10(count)+1)*scalingFactor
      normalizedCount.toFloat
    }

    WeightedLocation(loc, computeWeightFactor)
  }
}
