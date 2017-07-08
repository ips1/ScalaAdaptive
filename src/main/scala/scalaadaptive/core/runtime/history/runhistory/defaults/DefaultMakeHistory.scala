package scalaadaptive.core.runtime.history.runhistory.defaults

import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.{DefaultFilterRunHistory, ImmutableFullRunHistory, RunHistory}
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 7/7/17.
  */
trait DefaultMakeHistory[TMeasurement] extends DefaultFilterRunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override protected def makeHistoryFromFilteredItems(items: Iterable[EvaluationData[TMeasurement]]): RunHistory[TMeasurement] = {
    val descriptors = items.filter(i => i.inputDescriptor.isDefined).map(i => i.inputDescriptor.get)
    val min = if (descriptors.nonEmpty) Some(descriptors.min) else None
    val max = if (descriptors.nonEmpty) Some(descriptors.max) else None
    new ImmutableFullRunHistory[TMeasurement](key, items, min, max)(num)
  }
}
