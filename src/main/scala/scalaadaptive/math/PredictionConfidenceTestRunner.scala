package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.core.logging.Logger
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by Petr Kubat on 6/25/17.
  */
class PredictionConfidenceTestRunner(val logger: Logger) extends RegressionConfidenceTestRunner {

  private def intervalsOverlap(int1: (Double, Double), int2: (Double, Double)) =
    int1._1 <= int2._2 && int2._1 <= int1._2

  /**
    * Runs a test based on the confidence intervals for the regression prediction. We have two SimpleRegressions and
    * want to find if the first regression prediction for value x is significantly lower or higher. We compute the
    * confidence intervals of significance alpha for the prediction of value x for the two regressions and compare them.
    * If they intersect, we can't decide. Otherwise, we compare the predictions.
    *
    * @param firstRegression  First regression that we want to test (and that will determine the result)
    * @param secondRegression Second regression that we want to test
    * @param point            Point for which we are testing the predictions
    * @param alpha            Significance level of the test
    * @return Result of the test
    */
  override def runTest(firstRegression: SimpleTestableRegression,
                       secondRegression: SimpleTestableRegression,
                       point: Double,
                       alpha: Double): Option[TestResult] = {

    if (alpha < 0.0 || alpha > 1.0) {
      logger.log(s"Invalid alpha provided to RegressionConfidenceTestRunner: $alpha")
      return None
    }

    logger.log(s"Comparing confidence intervals on significance level $alpha")

    val firstInterval = try firstRegression.predictInterval(point, alpha) catch {
      case e: Exception => {
        logger.log(s"Exception during confidence interval computation: $e")
        return None
      }
    }
    logger.log(s"First interval for x = $point: $firstInterval")

    val secondInterval = try secondRegression.predictInterval(point, alpha) catch {
      case e: Exception => {
        logger.log(s"Exception during confidence interval computation: $e")
        return None
      }
    }
    logger.log(s"Second interval for x = $point: $secondInterval")

    for {
      first <- firstInterval
      second <- secondInterval

      result <- if (intervalsOverlap(first, second)) {
        logger.log(s"Intervals overlap, can't decide")
        Some(TestResult.CantRejectEquality)
      } else if (first._1 < second._1)
        Some(TestResult.ExpectedLower)
      else
        Some(TestResult.ExpectedHigher)
    } yield result
  }

  /**
    * Runs a series of tests to find out if the prediction for value x of the first regression is significantly lower
    * or higher than predictions of n other regressions. Runs the simple test n times.
    *
    * @param firstRegression  First regression we want to test
    * @param otherRegressions The rest of regression that we want to test against
    * @param point            Point for which we are testing the predictions
    * @param alpha            Significance level of the test
    * @return Result of the test
    */
  override def runTest(firstRegression: SimpleTestableRegression,
                       otherRegressions: Seq[SimpleTestableRegression],
                       point: Double,
                       alpha: Double): Option[TestResult] = {
    // We need to perform one test for each remaining sample
    val numTests = otherRegressions.size

    if (numTests <= 0)
      None

    val testResults = otherRegressions
      .map(second => runTest(firstRegression, second, point, alpha))

    if (testResults.forall(res => res.contains(TestResult.ExpectedHigher)))
      return Some(TestResult.ExpectedHigher)

    if (testResults.forall(res => res.contains(TestResult.ExpectedLower)))
      return Some(TestResult.ExpectedLower)

    if (testResults.exists(res => res.isEmpty))
      return None

    Some(TestResult.CantRejectEquality)
  }
}
