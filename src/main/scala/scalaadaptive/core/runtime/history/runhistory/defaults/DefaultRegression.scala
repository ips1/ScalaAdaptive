package scalaadaptive.core.runtime.history.runhistory.defaults

import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.extensions.Averageable
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by Petr Kubat on 7/1/17.
  */
trait DefaultRegression[TMeasurement] extends RunHistory[TMeasurement] {
  protected val num: Averageable[TMeasurement]

  override def runRegression: SimpleTestableRegression = {
    val regression = new SimpleTestableRegression()
    runItems
      .filter(i => i.inputDescriptor.isDefined)
      .foreach(i => regression.addData(i.inputDescriptor.get, num.toDouble(i.measurement)))
    regression
  }
}
