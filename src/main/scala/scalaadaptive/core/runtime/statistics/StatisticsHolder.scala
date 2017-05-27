package scalaadaptive.core.runtime.statistics

import scalaadaptive.core.runtime.RunData

/**
  * Created by pk250187 on 5/27/17.
  */
trait StatisticsHolder[TArgType, TRetType]
  extends StatisticDataProvider with StatisticFunctionProvider[TArgType, TRetType]{
  def applyRunData(data: RunData): Unit
}
