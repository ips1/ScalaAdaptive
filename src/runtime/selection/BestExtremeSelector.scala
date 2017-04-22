package runtime.selection

import runtime.history.RunHistory

/**
  * Created by pk250187 on 4/22/17.
  */
class BestExtremeSelector extends RunSelector[Long] {
  override def selectOption(records: Seq[RunHistory[Long]]): RunHistory[Long] =
    records.minBy(x => x.best())
}
