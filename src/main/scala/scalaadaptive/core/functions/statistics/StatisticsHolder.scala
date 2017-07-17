package scalaadaptive.core.functions.statistics

import scalaadaptive.api.policies.StatisticDataProvider
import scalaadaptive.core.functions.RunData

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A complex type that allows collecting and presenting statistics about the runs of an adaptive function.
  *
  */
trait StatisticsHolder[TArgType, TRetType]
  extends StatisticDataProvider with StatisticFunctionProvider[TArgType, TRetType]{

  /**
    * Updates the statistics with new run data.
    * @param data The run data.
    * @param markAsGather True if the run should be marked as gather in the statistics.
    */
  def applyRunData(data: RunData, markAsGather: Boolean): Unit

  /** Increases the total run count in the statistics */
  def markRun(): Unit
}
