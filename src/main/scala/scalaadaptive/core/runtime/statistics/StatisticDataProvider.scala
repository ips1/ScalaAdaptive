package scalaadaptive.core.runtime.statistics

/**
  * Created by pk250187 on 5/27/17.
  */
trait StatisticDataProvider {
  def getTotalRunCount: Long
  def getStreakLength: Long
  def getTotalOverheadTime: Long
  def getAccumulatedOverheadTime: Long

  def resetAccumulatedOverheadTime(): Unit
}
