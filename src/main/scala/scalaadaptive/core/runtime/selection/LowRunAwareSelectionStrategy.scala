package scalaadaptive.core.runtime.selection

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 4/22/17.
  */
class LowRunAwareSelectionStrategy[TMeasurement](val logger: Logger,
                                                 val lowRunSelector: SelectionStrategy[TMeasurement],
                                                 val normalSelector: SelectionStrategy[TMeasurement],
                                                 val lowRunLimit: Int) extends SelectionStrategy[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using LowRunAwareSelector")

    val lowRunOptions = records.filter(_.runCount < lowRunLimit)

    if (lowRunOptions.nonEmpty) {
      logger.log(s"LowRunAwareSelector: Not enough data for: " +
        s"${lowRunOptions.map(o => s"${o.identifier} - ${o.runCount}").mkString(", ")}")
      return lowRunSelector.selectOption(lowRunOptions, inputDescriptor)
    }

    normalSelector.selectOption(records, inputDescriptor)
  }
}
