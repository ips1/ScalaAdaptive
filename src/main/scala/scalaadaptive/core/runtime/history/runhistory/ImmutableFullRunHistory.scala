package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.runhistory.defaults._
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/1/17.
  */
class ImmutableFullRunHistory[TMeasurement] (override val key: HistoryKey,
                                             override val runItems: Iterable[EvaluationData[TMeasurement]],
                                             override val minDescriptor: Option[Long],
                                             override val maxDescriptor: Option[Long])
                                           (implicit override val num: Averageable[TMeasurement])
  extends RunHistory[TMeasurement]
    with DefaultGrouping[TMeasurement]
    with DefaultAverage[TMeasurement]
    with DefaultBest[TMeasurement]
    with DefaultStatistics[TMeasurement]
    with DefaultRegression[TMeasurement] {

  def this(key: HistoryKey)(implicit num: Averageable[TMeasurement]) =
    this(key, List(), None, None)

  override def runCount: Int = runItems.size
  override def applyNewRun(runResult: EvaluationData[TMeasurement]): ImmutableFullRunHistory[TMeasurement] = {
    val newItems = runResult :: runItems.toList
    val newMinDescriptor = runResult.inputDescriptor match {
      case Some(descriptor) =>
        if (minDescriptor.isEmpty || descriptor < minDescriptor.get) Some(descriptor) else minDescriptor
      case None => minDescriptor
    }
    val newMaxDescriptor = runResult.inputDescriptor match {
      case Some(descriptor) =>
        if (maxDescriptor.isEmpty || descriptor > maxDescriptor.get) Some(descriptor) else maxDescriptor
      case None => maxDescriptor
    }
    new ImmutableFullRunHistory[TMeasurement](key, newItems, newMinDescriptor, newMaxDescriptor)
  }

  override def takeWhile(filter: (EvaluationData[TMeasurement]) => Boolean): RunHistory[TMeasurement] = {
    val filteredItems = runItems.takeWhile(filter)
    val descriptors = filteredItems.filter(i => i.inputDescriptor.isDefined).map(i => i.inputDescriptor.get)
    val min = if (descriptors.nonEmpty) Some(descriptors.min) else None
    val max = if (descriptors.nonEmpty) Some(descriptors.max) else None
    new ImmutableFullRunHistory[TMeasurement](key, filteredItems, min, max)
  }
}
