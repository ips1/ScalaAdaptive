package scalaadaptive.core.runtime.history.runhistory.defaults

import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/1/17.
  */
trait DefaultAverage[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def average(): Option[Double] = num.average(runItems.map(i => i.measurement)).map(num.toDouble)
}
