package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger
import scalaadaptive.math.TTestResult.TTestResult

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
    * from a distribution with higher mean than the second sample.
    * Possible results:
    * TestResult.HigherMean - expecting the first sample to have higher mean, rejecting the hypothesis
    * that the first sample has lower mean
    * TestResult.LowerMean - expecting the first sample to have lower mean, rejecting the hypothesis
    * that the first sample has higher mean
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param sampleStats1 First sample data statistics
    * @param sampleStats2 Second sample data statistics
    * @param alpha        Significance level of the test
    * @return Test result or None in case of an Error
    */
  override def runTest(sampleStats1: StatisticalSummary,
              sampleStats2: StatisticalSummary,
              alpha: Double): Option[TTestResult] = {

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
      return Some(TTestResult.HigherMean)
    }
    else if (tValue < -1 * quantile) {
      return Some(TTestResult.LowerMean)
    }

    logger.log(s"Can't reject")

    Some(TTestResult.CantRejectEquality)
  }
}
