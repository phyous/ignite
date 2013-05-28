package lib

trait GeoCalculation {
  private[this] val RAD_PER_DEG = 0.017453293
  private[this] val Rmiles = 3956

  /**
   * Calculate the distance between two points on a sphere
   * @param p1 First point
   * @param p2 Second point
   * @return Distance in miles
   */
  def haversineDistance(p1: Location, p2: Location): Double = {
    val dLon = p2.longitude - p1.longitude
    val dLat = p2.latitude - p1.latitude
    val dLonRad = dLon * RAD_PER_DEG
    val dLatRad = dLat * RAD_PER_DEG
    val lat1Rad = p1.latitude * RAD_PER_DEG
    val lat2Rad = p2.latitude * RAD_PER_DEG

    val a = Math.pow(Math.sin(dLatRad / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLonRad / 2), 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    Rmiles * c
  }
}
