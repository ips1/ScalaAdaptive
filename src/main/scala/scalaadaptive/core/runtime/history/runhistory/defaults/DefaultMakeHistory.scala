package scalaadaptive.core.runtime.history.runhistory.defaults

import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.{DefaultFilterRunHistory, ImmutableFullRunHistory, RunHistory}
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 7/7/17.
  *
  * Mixin with default implementation of the makeHistoryFromFilteredItems method.
  *
  * Creates an [[scalaadaptive.core.runtime.history.runhistory.ImmutableFullRunHistory]] from the data. It can be used
  * basically with any [[scalaadaptive.core.runtime.history.runhistory.RunHistory]], because the history after filtering
  * will not be modified anywhere, so the internal representation does not matter.
  *
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
