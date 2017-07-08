package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.core.functions.RunData
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.{ClosestProvider, WindowSizeProvider}
import scalaadaptive.math._

/**
  * Created by pk250187 on 5/20/17.
  */
class RegressionSelectionStrategy[TMeasurement](val logger: Logger,
                                                val testRunner: RegressionConfidenceTest,
                                                val secondarySelector: SelectionStrategy[TMeasurement],
                                                val alpha: Double)(implicit num: Numeric[TMeasurement])
  extends SelectionStrategy[TMeasurement] {

  private def createRegression(runData: Seq[EvaluationData[TMeasurement]]): SimpleTestableRegression = {
    val regression = new SimpleTestableRegression()
    logger.log(s"Creating regression from ${runData.length} entries.")

    runData.foreach(i => regression.addData(i.inputDescriptor.get, num.toDouble(i.measurement)))
    regression
  }

  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): RunHistory[TMeasurement] = {
    logger.log("Selecting using WindowBoundRegressionSelectionStrategy")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return records.head
    }

    val regressions = records.map(r => (r, r.runRegression))

    val positiveResults = regressions
      .view
      .map(regression => {
        val remaining = regressions
          .filter(r => r != regression)
          .map(_._2)

        val result = testRunner.runTest(regression._2, remaining, descriptor.toDouble, alpha)

        (regression, result)
      })

    positiveResults
      .find(_._2.contains(TestResult.ExpectedLower))
      .map(_._1._1)
      .getOrElse(secondarySelector.selectOption(records, inputDescriptor))
  }
}
