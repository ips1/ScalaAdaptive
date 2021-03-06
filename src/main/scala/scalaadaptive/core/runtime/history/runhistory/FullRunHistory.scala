package scalaadaptive.core.runtime.history.runhistory

import scala.collection.mutable.ArrayBuffer
import scalaadaptive.core.runtime.history.runhistory.defaults._
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 3/21/17.
  *
  * An implementation of [[RunHistory]] that stores the data in a mutable [[ArrayBuffer]].
  * Adding new data returns the same instance which gets mutated.
  *
  * The implementation is NOT thread-safe.
  *
  * All the additional data methods are implemented using mixins that count the values. For caching the data, wrap the
  * history into a caching wrapper (see [[CachedGroupedRunHistory]], [[CachedRegressionRunHistory]],
  * [[CachedStatisticsRunHistory]])
  *
  * @param key The key of the function.
  */
class FullRunHistory[TMeasurement] (override val key: HistoryKey)
                                  (implicit override val num: Averageable[TMeasurement])
  extends DefaultFilterRunHistory[TMeasurement]
    with DefaultGrouping[TMeasurement]
    with DefaultStatistics[TMeasurement]
    with DefaultRegression[TMeasurement]
    with DefaultMakeHistory[TMeasurement] {

  private val internalRunItems: ArrayBuffer[EvaluationData[TMeasurement]] =
    new ArrayBuffer[EvaluationData[TMeasurement]]()
  private var minDesc: Option[Long] = None
  private var maxDesc: Option[Long] = None

  override def runCount: Int = internalRunItems.size
  override def applyNewRun(runResult: EvaluationData[TMeasurement]): FullRunHistory[TMeasurement] = {
    internalRunItems.append(runResult)
    runResult.inputDescriptor match {
      case Some(descriptor) =>
        if (minDesc.isEmpty || descriptor < minDesc.get) minDesc = Some(descriptor)
        if (maxDesc.isEmpty || descriptor > maxDesc.get) maxDesc = Some(descriptor)
      case None =>
    }
    this
  }

  override def runItems: Iterable[EvaluationData[TMeasurement]] = internalRunItems.reverseIterator.toIterable

  override def minDescriptor: Option[Long] = minDesc
  override def maxDescriptor: Option[Long] = maxDesc
}
