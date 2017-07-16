package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 5/20/17.
  */
class LeastDataSelectionStrategy[TMeasurement](val logger: Logger) extends SelectionStrategy[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using LeastDataSelector")
    records.minBy(r => r.runCount).key
  }
}
