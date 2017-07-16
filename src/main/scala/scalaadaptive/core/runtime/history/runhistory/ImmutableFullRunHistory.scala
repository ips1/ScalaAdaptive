package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.runhistory.defaults._
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 5/1/17.
  */
class ImmutableFullRunHistory[TMeasurement] (override val key: HistoryKey,
                                             override val runItems: Iterable[EvaluationData[TMeasurement]],
                                             override val minDescriptor: Option[Long],
                                             override val maxDescriptor: Option[Long])
                                           (implicit override val num: Averageable[TMeasurement])
  extends DefaultFilterRunHistory[TMeasurement]
    with DefaultGrouping[TMeasurement]
    with DefaultStatistics[TMeasurement]
    with DefaultRegression[TMeasurement]
    with DefaultMakeHistory[TMeasurement] {

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
}
