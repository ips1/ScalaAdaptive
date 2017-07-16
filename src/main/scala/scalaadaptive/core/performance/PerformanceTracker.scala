package scalaadaptive.core.performance

/**
  * Created by Petr Kubat on 5/1/17.
  */
trait PerformanceTracker {
  def addStoringTime(time: Long)
  def addSelectionTime(time: Long)
  def addSelectionTime()
  def addStoringTime()
  def addFunctionTime(time: Long)
  def addFunctionTime()
  def startTracking()
  def reset()
  def getPerformance: PerformanceProvider
}
