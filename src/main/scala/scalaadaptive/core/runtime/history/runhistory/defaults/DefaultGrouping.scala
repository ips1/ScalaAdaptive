package scalaadaptive.core.runtime.history.runhistory.defaults

import scalaadaptive.core.runtime.history.evaluation.data.GroupedEvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * Mixin with default implementation of computing the grouped averages.
  *
  */
trait DefaultGrouping[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]] = runItems
    .filter(i => i.inputDescriptor.isDefined)
    .groupBy(i => i.inputDescriptor.get)
    .map(v => {
      val average = num.average(v._2.map(i => i.measurement))
      average match {
        case Some(avg) => Some((v._1, new GroupedEvaluationData[TMeasurement](avg, v._2.size)))
        case _ => None
      }
    })
    .filter(i => i.isDefined)
    .map(i => (i.get._1, i.get._2))
    .toMap
}
