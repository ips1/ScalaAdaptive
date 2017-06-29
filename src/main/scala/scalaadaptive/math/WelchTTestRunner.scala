package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.inference.{TTest, TestUtils}

import scalaadaptive.core.logging.Logger
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by pk250187 on 5/2/17.
  */
class WelchTTestRunner(val logger: Logger) extends TwoSampleTTest {

  private def logSampleDetails(sample: StatisticalSummary): Unit = {
    logger.log(s"mean: ${sample.getMean}")
    logger.log(s"variance: ${sample.getVariance}")
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
              alpha: Double): Option[TestResult] = {

    if (alpha < 0.0 || alpha > 1.0) {
      logger.log(s"Invalid alpha provided to TTestRunner: $alpha")
      return None
    }

    logger.log(s"Performing test on significance level $alpha")

    logger.log(s"Sample 1")
    logSampleDetails(sampleStats1)
    logger.log(s"Sample 2")
    logSampleDetails(sampleStats2)

    val test = new TTest()
    // We need to use 2 * alpha for one-sided testing
    val result = try {
      test.tTest(sampleStats1, sampleStats2, alpha * 2)
    } catch {
      case e: Exception => {
        logger.log(s"Exception during t-test: $e")
        return None
      }
    }

    if (!result) {
      // Can't decide about the one-sided alternative
      logger.log(s"Can't reject")
      return Some(TestResult.CantRejectEquality)
    }

    // Equality was rejected, we can accept the one-sided alternative
    if (sampleStats1.getMean > sampleStats2.getMean)
      Some(TestResult.ExpectedHigher)
    else
      Some(TestResult.ExpectedLower)
  }
}