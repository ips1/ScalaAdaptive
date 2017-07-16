package scalaadaptive.core.performance

/**
  * Created by Petr Kubat on 5/20/17.
  */
trait PerformanceProvider {
  def getFunctionTime: Long
  def getOverheadTime: Long
  def getOverheadPercentage: Double
  def toLogString: String
}