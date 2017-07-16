package scalaadaptive.api.policies

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A provider of statistical data about an adaptive function (see [[scalaadaptive.api.functions.AdaptiveFunction0]]
  * and corresponding types) invocation history.
  *
  */
trait StatisticDataProvider {
  /** Total number of times the adaptive function was invoked */
  def getTotalRunCount: Long
  /** Total number of times the [[scalaadaptive.api.policies.PolicyResult.GatherData]] was received performed */
  def getTotalGatherCount: Long
  /** Total number of times the [[scalaadaptive.api.policies.PolicyResult.SelectNew]] was received performed */
  def getTotalSelectCount: Long
  /** The number of times that the last selected function was selected in a row */
  def getStreakLength: Long
  /** Total time spent on selection and invocation process for this function (including the implementation execution
    * time) */
  def getTotalTime: Long
  /** Total time spent on the implementation execution time */
  def getTotalFunctionTime: Long
  /** Total time spent on the selection process overhead */
  def getTotalOverheadTime: Long
  /** Total time spent handling the [[scalaadaptive.api.policies.PolicyResult.GatherData]] result (including the
    * implementation execution time) */
  def getTotalGatherTime: Long
  /** Total number of times the last selected function was selected */
  def getLastSelectCount: Long
  /** Total number of times the most selected function was selected */
  def getMostSelectCount: Long
}
