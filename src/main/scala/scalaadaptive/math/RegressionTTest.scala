package scalaadaptive.math

import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.math.RegressionTTestResult.RegressionTTestResult
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by pk250187 on 6/7/17.
  */
trait RegressionTTest {
  /**
    * Runs a T-Test against the estimated slope of the provided regression. Hypothesis are following:
    * H0: slope = 0 -> no linear relationship exists, regression has no significance
    * H1: slope != 0 -> relationship exists, regression is valid
    * @param regression Regression that we want to test
    * @param alpha Significance level of the test
    * @return Result of the test
    */
  def runTest(regression: SimpleRegression,
              alpha: Double): Option[RegressionTTestResult]
}
