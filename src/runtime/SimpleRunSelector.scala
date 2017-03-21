package runtime

import options.RunOption
import runtime.history.{RunData, RunHistory}

/**
  * Created by pk250187 on 3/19/17.
  */
class SimpleRunSelector() extends RunSelector[Long] {
  val lowRunLimit = 15

  private def aggregate(data: Seq[RunData[Long]]) = data.map(_.measurement).sum / data.size
  private def select(records: Seq[RunHistory[Long]]) = records.minBy(x => aggregate(x.runItems))

  override def selectOption(records: Seq[RunHistory[Long]]): RunHistory[Long] = {
    val lowRunOptions = records.filter(_.runCount < lowRunLimit)

    if (lowRunOptions.nonEmpty) {
      return lowRunOptions.head
    }

    return select(records)
  }
}
