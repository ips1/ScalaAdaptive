package scalaadaptive.core.runtime.selection

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 4/22/17.
  */
class BestExtremeSelector extends RunSelector[Long] {
  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): RunHistory[Long] = {
    logger.log("Selecting using BestExtremeSelector")
    records.minBy(x => x.best())
  }
}
