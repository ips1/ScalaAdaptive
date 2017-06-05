package scalaadaptive.core.functions.statistics

/**
  * Created by pk250187 on 5/27/17.
  */
trait StatisticDataProvider {
  def getTotalRunCount: Long
  def getStreakLength: Long
  def getTotalTime: Long
  def getTotalOverheadTime: Long
  def getTotalGatherTime: Long
  def getLastRunCount: Long
}
