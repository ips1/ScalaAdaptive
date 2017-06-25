package scalaadaptive.math

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.math.RegressionTTestResult.RegressionTTestResult
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by pk250187 on 6/25/17.
  */
trait RegressionConfidenceTest {
  /**
    * Runs a test based on the confidence intervals for the regression prediction. We have two SimpleRegressions and
    * want to find if the first regression prediction for value x is significantly lower or higher. We compute the
    * confidence intervals of significance alpha for the prediction of value x for the two regressions and compare them.
    * If they intersect, we can't decide. Otherwise, we compare the predictions.
    * @param firstRegression First regression that we want to test (and that will determine the result)
    * @param secondRegression Second regression that we want to test
    * @param point Point for which we are testing the predictions
    * @param alpha Significance level of the test
    * @return Result of the test
    */
  def runTest(firstRegression: SimpleTestableRegression,
              secondRegression: SimpleTestableRegression,
              point: Double,
              alpha: Double): Option[TestResult]

  /**
    * Runs a series of tests to find out if the prediction for value x of the first regression is significantly lower
    * or higher than predictions of n other regressions. Runs the simple test n times.
    * @param firstRegression First regression we want to test
    * @param otherRegressions The rest of regression that we want to test against
    * @param point Point for which we are testing the predictions
    * @param alpha Significance level of the test
    * @return Result of the test
    */
  def runTest(firstRegression: SimpleTestableRegression,
              otherRegressions: Seq[SimpleTestableRegression],
              point: Double,
              alpha: Double): Option[TestResult]
}
