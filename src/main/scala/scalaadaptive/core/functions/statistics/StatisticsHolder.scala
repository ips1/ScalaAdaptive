package scalaadaptive.core.functions.statistics

import scalaadaptive.api.policies.StatisticDataProvider
import scalaadaptive.core.functions.RunData

/**
  * Created by Petr Kubat on 5/27/17.
  */
trait StatisticsHolder[TArgType, TRetType]
  extends StatisticDataProvider with StatisticFunctionProvider[TArgType, TRetType]{
  def applyRunData(data: RunData, markAsGather: Boolean): Unit
  def markRun(): Unit
}
