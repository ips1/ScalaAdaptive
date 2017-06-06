package scalaadaptive.core.runtime.selection

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 3/19/17.
  */
class BestAverageSelector(val logger: Logger) extends RunSelector[Long] {
  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): RunHistory[Long] = {
    logger.log("Selecting using BestAverageSelector")
    records.minBy(x => x.average())
  }
}
