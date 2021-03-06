package scalaadaptive.core.runtime.history.runhistory.defaults

import org.apache.commons.math3.stat.descriptive.{StatisticalSummary, SummaryStatistics}

import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * Mixin with default statistics data computation implementation.
  *
  */
trait DefaultStatistics[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def runStatistics: StatisticalSummary = {
    val s = new SummaryStatistics()
    runItems.foreach(i => s.addValue(num.toDouble(i.measurement)))
    s
  }
}
