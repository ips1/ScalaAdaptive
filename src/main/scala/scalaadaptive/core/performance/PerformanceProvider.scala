package scalaadaptive.core.performance

/**
  * Created by Petr Kubat on 5/20/17.
  *
  * Holder for the performance data measured by the [[PerformanceTracker]].
  *
  * Provides the following times:
  * - overhead time (how long it takes to select a function using a
  * [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] and store evaluation after invocation)
  * - function time (how long it takes to invoke and evaluate the function)
  *
  */
trait PerformanceProvider {
  /** Gets total function time */
  def getFunctionTime: Long
  /** Gets total overhead time */
  def getOverheadTime: Long

  /** Gets the ratio of overhead and function time */
  def getOverheadPercentage: Double
  /** Creates a log string from the data */
  def toLogString: String
}