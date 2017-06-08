package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.math.{RegressionTTest, RegressionTTestResult}

/**
  * Created by pk250187 on 5/20/17.
  */
class LimitedRegressionSelector[TMeasurement](val logger: Logger,
                                              val maxWindowSize: Long,
                                              val testRunner: RegressionTTest,
                                              val alpha: Double)(implicit num: Numeric[TMeasurement])
  extends RunSelector[TMeasurement] {

  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): RunHistory[TMeasurement] = {
    logger.log("Selecting using LimitedRegressionSelector")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return records.head
    }

    val maxDifference = maxWindowSize / 2

    val regressions = records.map(r => {
      val regression = new SimpleRegression()
      r.runItems
        .filter(i => i.inputDescriptor.isDefined && Math.abs(i.inputDescriptor.get - descriptor) < maxDifference)
        .foreach(i => {
          //println(s"Adding ${i.inputDescriptor.get}: ${num.toDouble(i.measurement)}")
          regression.addData(i.inputDescriptor.get, num.toDouble(i.measurement))
        })
      (r, regression)
    })

    // Results with not enough significance:
    val insignificantResults = regressions.filter(r =>
      r._2.getN <= 3 || !testRunner.runTest(r._2, alpha).contains(RegressionTTestResult.RelationshipExists)
    )

    if (insignificantResults.nonEmpty) {
      val first = insignificantResults.head._1
      logger.log(s"Not enough data to predict ${first.key.function}")
      return first
    }

    regressions.minBy(p => p._2.predict(descriptor))._1
  }
}
