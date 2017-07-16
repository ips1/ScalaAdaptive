package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.core.functions.RunData
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.selection.support.{ClosestProvider, WindowSizeProvider}
import scalaadaptive.math.{RegressionTTest, RegressionTTestResult}

/**
  * Created by Petr Kubat on 5/20/17.
  */
class ExperimentalLimitedRegressionSelectionStrategy[TMeasurement](val logger: Logger,
                                                                   val windowSizeSelector: WindowSizeProvider[TMeasurement],
                                                                   val testRunner: RegressionTTest,
                                                                   val secondarySelector: SelectionStrategy[TMeasurement],
                                                                   val alpha: Double)(implicit num: Numeric[TMeasurement])
  extends SelectionStrategy[TMeasurement] {

  private def createRegression(runData: Seq[EvaluationData[TMeasurement]]): SimpleRegression = {
    val regression = new SimpleRegression()
    logger.log(s"Creating regression from ${runData.length} entries.")

    runData.foreach(i => regression.addData(i.inputDescriptor.get, num.toDouble(i.measurement)))
    regression
  }

  private def isRegressionSignificant(regression: SimpleRegression): Boolean =
    regression.getN > 3 && testRunner.runTest(regression, alpha).contains(RegressionTTestResult.RelationshipExists)

  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using LimitedRegressionSelector")

    val descriptor = inputDescriptor match {
      case Some(d) => d
      case _ => return records.head.key
    }

    val regressions = records.map(r => {
      // TODO: Is the keys guaranteed to be sorted?
      //      val windowSize = windowSizeSelector.selectWindowSize(
      //        r.runItems.filter(k => k.inputDescriptor.isDefined).map(k => k.inputDescriptor.get).toList.sorted)
      //      logger.log(s"Window size for limited selection: $windowSize")
      //      val maxDifference = windowSize / 2
      //      val selectedItems = r.runItems
      //        .filter(i => i.inputDescriptor.isDefined && Math.abs(i.inputDescriptor.get - descriptor) < maxDifference)
      //      (r, createRegression(selectedItems.toList))

      val regression = new SimpleRegression()
      val closestProvider = new ClosestProvider[EvaluationData[TMeasurement]](
        r.runItems.filter(k => k.inputDescriptor.isDefined).toArray.sortBy(k => k.inputDescriptor.get),
        _.inputDescriptor.get,
        descriptor
      )
      var next = closestProvider.getNext
      while (next.isDefined && !isRegressionSignificant(regression)) {
        regression.addData(next.get.inputDescriptor.get, num.toDouble(next.get.measurement))
        next = closestProvider.getNext
      }

      (r, regression)
    })

    // Results with not enough significance:
    val insignificantResults = regressions.filter(r => !isRegressionSignificant(r._2))

    if (insignificantResults.nonEmpty) {
      val first = insignificantResults.head._1
      logger.log(s"Not enough data to predict ${first.key.functionId}")
      return secondarySelector.selectOption(records, inputDescriptor)
    }

    regressions.minBy(p => p._2.predict(descriptor))._1.key
  }
}
