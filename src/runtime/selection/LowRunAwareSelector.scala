package runtime.selection

import runtime.history.RunHistory

/**
  * Created by pk250187 on 4/22/17.
  */
class LowRunAwareSelector[TMeasurement](val innerSelector: RunSelector[TMeasurement],
                                        val lowRunLimit: Int) extends RunSelector[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Long): RunHistory[TMeasurement] = {
    val lowRunOptions = records.filter(_.runCount < lowRunLimit)

    if (lowRunOptions.nonEmpty) {
      return lowRunOptions.head
    }

    innerSelector.selectOption(records, inputDescriptor)
  }
}
