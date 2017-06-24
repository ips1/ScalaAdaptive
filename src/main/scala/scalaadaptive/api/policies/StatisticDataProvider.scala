package scalaadaptive.api.policies

/**
  * Created by pk250187 on 5/27/17.
  */
trait StatisticDataProvider {
  def getTotalRunCount: Long
  def getTotalGatherCount: Long
  def getTotalSelectCount: Long
  def getStreakLength: Long
  def getTotalTime: Long
  def getTotalFunctionTime: Long
  def getTotalOverheadTime: Long
  def getTotalGatherTime: Long
  def getLastRunCount: Long
  def getMostRunCount: Long
}
