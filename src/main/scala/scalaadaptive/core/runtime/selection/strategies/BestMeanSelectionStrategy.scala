package scalaadaptive.core.runtime.selection.strategies

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 3/19/17.
  *
  * A strategy that selects the function according to the best mean without any statistical testing.
  *
  * @param logger Logger used to log the selection process.
  *
  */
class BestMeanSelectionStrategy(val logger: Logger) extends SelectionStrategy[Long] {
  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using BestAverageSelector")
    records.minBy(x => x.mean).key
  }
}
