package runtime.selection

import runtime.history.{RunData, RunHistory}

/**
  * Created by pk250187 on 3/19/17.
  */
class SimpleRunSelector(val lowRunLimit: Int) extends RunSelector[Long] {
  private def select(records: Seq[RunHistory[Long]]) = records.minBy(x => x.average)

  override def selectOption(records: Seq[RunHistory[Long]]): RunHistory[Long] = {
    val lowRunOptions = records.filter(_.runCount < lowRunLimit)

    if (lowRunOptions.nonEmpty) {
      return lowRunOptions.head
    }

    return select(records)
  }
}
