package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 5/20/17.
  *
  * A selection strategy that selects the function that has least historical measurements.
  *
  * Usually used in combination with [[LowRunAwareSelectionStrategy]] or in the gather methods of
  * [[scalaadaptive.core.runtime.selection.AdaptiveSelector]]
  *
  * @param logger Logger used to log the selection process.
  *
  */
class LeastDataSelectionStrategy[TMeasurement](val logger: Logger) extends SelectionStrategy[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using LeastDataSelector")
    records.minBy(r => r.runCount).key
  }
}
