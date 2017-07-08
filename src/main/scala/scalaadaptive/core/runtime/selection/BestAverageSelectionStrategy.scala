package scalaadaptive.core.runtime.selection

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 3/19/17.
  */
class BestAverageSelectionStrategy(val logger: Logger) extends SelectionStrategy[Long] {
  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using BestAverageSelector")
    records.minBy(x => x.average()).key
  }
}
