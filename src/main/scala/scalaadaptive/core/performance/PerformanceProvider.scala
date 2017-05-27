package scalaadaptive.core.performance

/**
  * Created by pk250187 on 5/20/17.
  */
trait PerformanceProvider {
  def getFunctionTime: Long
  def getOverheadTime: Long
  def getOverheadPercentage: Double
}
