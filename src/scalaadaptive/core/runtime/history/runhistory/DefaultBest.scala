package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 5/1/17.
  */
trait DefaultBest[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def best(): Option[Double] =
    if (runItems.isEmpty) None else Some(num.toDouble(runItems.map(_.measurement).min(num)))
}
