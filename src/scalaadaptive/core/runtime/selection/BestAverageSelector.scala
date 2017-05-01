package scalaadaptive.core.runtime.selection

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 3/19/17.
  */
class BestAverageSelector extends RunSelector[Long] {
  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Long): RunHistory[Long] =
    records.minBy(x => x.average())
}