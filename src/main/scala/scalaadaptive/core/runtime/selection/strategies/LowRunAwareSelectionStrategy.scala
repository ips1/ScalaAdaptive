package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 4/22/17.
  *
  * A selection strategy that uses one strategy when at least one function has less than lowRunLimit historical data,
  * and a different one otherwise.
  *
  * It is usually used in combination with [[LeastDataSelectionStrategy]] as the lowRunStrategy to guarantee a minimum
  * amount of data for all the functions involved.
  *
  * @param logger Logger used to log the selection process.
  * @param lowRunStrategy The strategy that is used when at least one function has less than lowRunLimit historical
  *                       records.
  * @param normalStrategy The strategy that is used when all the function have more than lowRunLimit historical records.
  * @param lowRunLimit The limit of number of historical records that all the function have to reach before using
  *                    normalStrategy.
  */
class LowRunAwareSelectionStrategy[TMeasurement](val logger: Logger,
                                                 val lowRunStrategy: SelectionStrategy[TMeasurement],
                                                 val normalStrategy: SelectionStrategy[TMeasurement],
                                                 val lowRunLimit: Int) extends SelectionStrategy[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using LowRunAwareSelector")

    val lowRunOptions = records.filter(_.runCount < lowRunLimit)

    if (lowRunOptions.nonEmpty) {
      logger.log(s"LowRunAwareSelector: Not enough data for: " +
        s"${lowRunOptions.map(o => s"${o.identifier} - ${o.runCount}").mkString(", ")}")
      return lowRunStrategy.selectOption(lowRunOptions, inputDescriptor)
    }

    normalStrategy.selectOption(records, inputDescriptor)
  }
}
