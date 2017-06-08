package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.math.TwoSampleTestResult.TwoSampleTestResult

/**
  * Created by pk250187 on 5/2/17.
  */
class TwoSampleTTestRunner(val logger: Logger) extends TwoSampleTTest {
  private def getQuantile(count1: Long, count2: Long, alpha: Double): Double = {
    val degreesOfFreedom = count1 + count2 - 2

    val distribution = new TDistribution(degreesOfFreedom)
    distribution.inverseCumulativeProbability(1 - alpha)
  }

  /**
    * Runs a simple T-Test with the specified significance level to determine whether the first sample values are
    * from a distribution with higher expectation than the second sample.
    * Possible results:
    * TestResult.HigherExpectation - expecting the first sample to have higher expectation, rejecting the hypothesis
    * that the first sample has lower expectation
    * TestResult.LowerExpectation - expecting the first sample to have lower expectation, rejecting the hypothesis
    * that the first sample has higher expectation
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param sampleStats1 First sample data statistics
    * @param sampleStats2 Second sample data statistics
    * @param alpha        Significance level of the test
    * @return Test result or None in case of an Error
    */
  override def runTest(sampleStats1: StatisticalSummary,
              sampleStats2: StatisticalSummary,
              alpha: Double): Option[TwoSampleTestResult] = {

    if (alpha < 0.0 || alpha > 1.0) {
      logger.log(s"Invalid alpha provided to TTestRunner: $alpha")
      return None
    }

    val tValue = TestUtils.t(sampleStats1, sampleStats2)
    val quantile = getQuantile(sampleStats1.getN, sampleStats2.getN, alpha)

    logger.log(s"Performing test on significance level $alpha")
    logger.log(s"T-value = $tValue")
    logger.log(s"Quantile = $quantile")

    if (tValue > quantile) {
      return Some(TwoSampleTestResult.HigherExpectation)
    }
    else if (tValue < -1 * quantile) {
      return Some(TwoSampleTestResult.LowerExpectation)
    }

    logger.log(s"Can't reject")

    Some(TwoSampleTestResult.CantRejectEquality)
  }
}
