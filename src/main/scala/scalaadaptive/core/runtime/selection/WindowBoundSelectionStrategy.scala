package scalaadaptive.core.runtime.selection
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.WindowSizeProvider
import scalaadaptive.math.RegressionConfidenceTestRunner

/**
  * Created by Petr Kubat on 7/7/17.
  *
  * A wrapper selection strategy that will filter the history records, limiting them only to records with input
  * descriptors in specified "window" around current input descriptor value. Then, it will pass the filtered
  * history records onto an innerStrategy (another SelectionStrategy instance), and return its decision.
  *
  * The window will be applied even if there are no records left after the filtering. Only if the current
  * input descriptor is None, the filtering will be skipped.
  *
  * @tparam TMeasurement The type of measurement data stored in RunHistories.
  *
  * @param logger The logger it uses to log selection process information.
  * @param windowSizeSelector A provider that will dynamically determine the window size for each history.
  * @param innerStrategy An inner strategy that the decision is delegated to.
  *
  */
class WindowBoundSelectionStrategy[TMeasurement](val logger: Logger,
                                                 val windowSizeSelector: WindowSizeProvider[TMeasurement],
                                                 val innerStrategy: SelectionStrategy[TMeasurement])
  extends SelectionStrategy[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): HistoryKey = {
    val windowRecords =
      if (inputDescriptor.isEmpty) {
        logger.log(s"Input descriptor not specified")
        records
      }
      else
        records.map(r => {
          val windowSize = windowSizeSelector.selectWindowSize(r)
          val descriptor = inputDescriptor.get
          logger.log(s"Window size for limited selection: $windowSize")
          val maxDifference = windowSize / 2
          val filtered = r.filter(i => i.inputDescriptor.isDefined && Math.abs(i.inputDescriptor.get - descriptor) <= maxDifference)

          filtered
        })

    innerStrategy.selectOption(windowRecords, inputDescriptor)
  }
}
