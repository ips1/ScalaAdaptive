package scalaadaptive.core.runtime.selection.support

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by Petr Kubat on 7/7/17.
  */
class WindowFilter(logger: Logger) {
  def filterRecords[TMeasurement](records: Seq[RunHistory[TMeasurement]],
                                  windowSizeProvider: Option[WindowSizeProvider[TMeasurement]],
                                  inputDescriptor: Option[Long]): Seq[(RunHistory[TMeasurement], Iterable[EvaluationData[TMeasurement]])] =
    records.map(r => {
      if (windowSizeProvider.isEmpty || inputDescriptor.isEmpty)
        (r, r.runItems)
      else {
        val windowSize = windowSizeProvider.get
          .selectWindowSize(r)
        val descriptor = inputDescriptor.get
        logger.log(s"Window size for limited selection: $windowSize")
        val maxDifference = windowSize / 2
        val filtered = r.runItems
          .filter(i => i.inputDescriptor.isDefined && Math.abs(i.inputDescriptor.get - descriptor) < maxDifference)
        (r, filtered)
      }
    })
}
