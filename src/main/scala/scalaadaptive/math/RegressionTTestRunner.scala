package scalaadaptive.math
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.regression.SimpleRegression

import scalaadaptive.math.RegressionTTestResult.RegressionTTestResult

/**
  * Created by pk250187 on 6/7/17.
  */
class RegressionTTestRunner extends RegressionTTest {
  private def getQuantile(count: Long, alpha: Double): Double = {
    val degreesOfFreedom = count - 2

    val distribution = new TDistribution(degreesOfFreedom)
    distribution.inverseCumulativeProbability(1 - (alpha / 2))
  }

  /**
    * Runs a T-Test against the estimated slope of the provided regression. Hypothesis are following:
    * H0: slope = 0 -> no linear relationship exists, regression has no significance
    * H1: slope != 0 -> relationship exists, regression is valid
    *
    * @param regression Regression that we want to test
    * @param alpha      Significance level of the test
    * @return Result of the test
    */
  override def runTest(regression: SimpleRegression, alpha: Double): Option[RegressionTTestResult] = {
    val estimatedSlope = regression.getSlope
    val slopeError = regression.getSlopeStdErr

    val testStat = estimatedSlope.toDouble / slopeError
    val quantile = getQuantile(regression.getN, alpha)

    if (testStat > (-1 * quantile) && testStat < quantile)
      Some(RegressionTTestResult.CantRejectNoRelationship)
    else
      Some(RegressionTTestResult.RelationshipExists)
  }
}
