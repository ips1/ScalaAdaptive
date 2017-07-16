package scalaadaptive.core.runtime.history.runhistory.defaults

import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 5/1/17.
  */
trait DefaultBest[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def best(): Option[Double] =
    if (runItems.isEmpty) None else Some(num.toDouble(runItems.map(_.measurement).min(num)))
}
