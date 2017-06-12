package scalaadaptive.core.functions.statistics

import scalaadaptive.core.functions.RunData

/**
  * Created by pk250187 on 5/27/17.
  */
trait StatisticsHolder[TArgType, TRetType]
  extends StatisticDataProvider with StatisticFunctionProvider[TArgType, TRetType]{
  def applyRunData(data: RunData, markAsGather: Boolean): Unit
  def markRun(): Unit
}
