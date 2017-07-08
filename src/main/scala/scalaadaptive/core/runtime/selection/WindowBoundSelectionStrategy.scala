package scalaadaptive.core.runtime.selection
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.WindowSizeProvider
import scalaadaptive.math.RegressionConfidenceTestRunner

/**
  * Created by pk250187 on 7/7/17.
  */
class WindowBoundSelectionStrategy[TMeasurement](val logger: Logger,
                                                 val windowSizeSelector: Option[WindowSizeProvider[TMeasurement]],
                                                 val innerStrategy: SelectionStrategy[TMeasurement])
                                                (implicit num: Numeric[TMeasurement]) extends SelectionStrategy[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]],
                            inputDescriptor: Option[Long]): RunHistory[TMeasurement] = {
    val windowRecords =
      if (windowSizeSelector.isEmpty || inputDescriptor.isEmpty)
        records
      else
        records.map(r => {
          val windowSize = windowSizeSelector.get
            .selectWindowSize(r)
          val descriptor = inputDescriptor.get
          logger.log(s"Window size for limited selection: $windowSize")
          val maxDifference = windowSize / 2
          r.filter(i => i.inputDescriptor.isDefined && Math.abs(i.inputDescriptor.get - descriptor) < maxDifference)
        })

    // TODO: Replace with mapping to original records?
    innerStrategy.selectOption(windowRecords, inputDescriptor)
  }
}
