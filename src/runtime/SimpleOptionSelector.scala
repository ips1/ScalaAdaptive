package runtime

import options.RunOption

/**
  * Created by pk250187 on 3/19/17.
  */
class SimpleOptionSelector extends OptionSelector[Long] {
  val lowRunLimit = 15

  private def aggregate(data: Seq[Long]) = data.sum / data.size
  private def select(records: Seq[Bucket[Long]]) = records.minBy(x => aggregate(x.runItems))

  override def selectOption(records: Seq[Bucket[Long]]): Bucket[Long] = {
    val lowRunOptions = records.filter(_.runCount < lowRunLimit)

    if (lowRunOptions.nonEmpty) {
      return lowRunOptions.head
    }

    return select(records)
  }
}
