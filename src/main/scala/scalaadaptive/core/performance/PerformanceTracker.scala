package scalaadaptive.core.performance

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * A supplementary type for tracking performance of certain parts of the invocation process.
  *
  * The three areas tracked are:
  * - selection time (how long it takes to select a function using a
  * [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]])
  * - function time (how long it takes to invoke and evaluate the function)
  * - storing time (how long it takes to store the evaluation data and update cached information)
  *
  */
trait PerformanceTracker {
  /** Adds the time to the total storing time */
  def addStoringTime(time: Long)
  /** Adds the time to the total selection time */
  def addSelectionTime(time: Long)
  /** Adds the time to the total function time */
  def addFunctionTime(time: Long)
  /** Adds elapsed time since the last startTracking or add* call to the total selection time */
  def addSelectionTime()
  /** Adds elapsed time since the last startTracking or add* call to the total storing time */
  def addStoringTime()
  /** Adds elapsed time since the last startTracking or add* call to the total function time */
  def addFunctionTime()
  /** Starts measuring time that can be later added using the add* methods */
  def startTracking()
  /** Resets the time measurement and all the values stored */
  def reset()
  /** Retrieves the measured times */
  def getPerformance: PerformanceProvider
}
