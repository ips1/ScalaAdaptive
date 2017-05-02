package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 5/2/17.
  */
class RegressionSelector[TMeasurement](implicit num: Numeric[TMeasurement])
  extends RunSelector[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Long): RunHistory[TMeasurement] = {

    val regressions = records.map(r => {
      val regression = new SimpleRegression()
      r.runItems.foreach(i => regression.addData(i.inputDescriptor, num.toDouble(i.measurement)))
      (r, regression)
    })

    // TODO: Not enough values?
    val min = regressions.minBy(p => p._2.predict(inputDescriptor))

    min._1
  }
}
