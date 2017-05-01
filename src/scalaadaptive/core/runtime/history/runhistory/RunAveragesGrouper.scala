package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.runtime.history.{GroupedRunData, RunData}
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/1/17.
  */
trait RunAveragesGrouper[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def runAveragesGroupedByDescriptor: Map[Long, GroupedRunData[TMeasurement]] = runItems
    .groupBy(i => i.inputDescriptor)
    .map(v => {
      val average = num.average(v._2.map(i => i.measurement))
      average match {
        case Some(avg) => Some(new GroupedRunData[TMeasurement](
          new RunData[TMeasurement](v._1, avg), v._2.size))
        case _ => None
      }
    })
    .filter(i => i.isDefined)
    .map(i => (i.get.averageRunData.inputDescriptor, i.get))
    .toMap
}
