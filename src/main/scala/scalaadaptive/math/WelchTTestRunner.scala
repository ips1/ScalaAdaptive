package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.inference.{TTest, TestUtils}

import scalaadaptive.core.logging.Logger
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * An implementation of [[TTestRunner]] that uses the Welch's T-test. Student's T-test can't be used due to unequal
  * variances of the samples.
  * For more information see the original thesis text.
  *
  */
class WelchTTestRunner(val logger: Logger) extends TTestRunner {

  private def logSampleDetails(sample: StatisticalSummary): Unit = {
    logger.log(s"mean: ${sample.getMean}")
    logger.log(s"variance: ${sample.getVariance}")
  }

  private def runTestInternal(sampleStats1: StatisticalSummary,
                              sampleStats2: StatisticalSummary,
                              oneSided: Boolean,
                              alpha: Double): Option[TestResult] = {
    if (alpha < 0.0 || alpha > 1.0) {
      logger.log(s"Invalid alpha provided to TTestRunner: $alpha")
      return None
    }

    logger.log(s"Performing T-test on significance level $alpha")

    logger.log(s"Sample 1")
    logSampleDetails(sampleStats1)
    logger.log(s"Sample 2")
    logSampleDetails(sampleStats2)

    val test = new TTest()
    val pValue = try {
      test.tTest(sampleStats1, sampleStats2)
    } catch {
      case e: Exception => {
        logger.log(s"Exception during t-test: $e")
        return None
      }
    }

    // We need to use 2 * alpha for one-sided testing
    val realAlpha = if (oneSided) alpha * 2 else alpha
    if (pValue > realAlpha) {
      // Can't decide about the one-sided alternative
      logger.log(s"Can't reject")
      return Some(TestResult.CantRejectEquality)
    }

    // Equality was rejected, we can accept the one-sided alternative
    if (sampleStats1.getMean > sampleStats2.getMean) {
      logger.log(s"Success with ExpectedHigher result")
      Some(TestResult.ExpectedHigher)
    }
    else {
      logger.log(s"Success with ExpectedLower result")
      Some(TestResult.ExpectedLower)
    }
  }

  /**
    * Runs a simple two-sided T-Test with the specified significance level to determine whether the first sample values are
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
              alpha: Double): Option[TestResult] =
    runTestInternal(sampleStats1, sampleStats2, oneSided = false, alpha)

  /**
    * Runs a series of T-Tests to determine whether the first sample is from a distribution with significantly
    * lower mean than all of the other samples. The test is one-sided!
    * The significance level specified is accumulated for the entire series.
    * Possible results:
    * TestResult.ExpectedLower - expecting the first sample to have lower mean than all remaining samples,
    * rejecting the hypothesis that the first sample has higher mean than all the remaining samples
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param firstSampleStats     First sample data statistics
    * @param remainingSampleStats Sequence of the remaining sample data statistics
    * @param alpha                Significance level of the test
    * @return Test result or None in case of an error
    */
  override def runTest(firstSampleStats: StatisticalSummary,
                       remainingSampleStats: Seq[StatisticalSummary],
                       alpha: Double): Option[TestResult] = {
    // We need to perform one test for each remaining sample
    val numTests = remainingSampleStats.size

    if (numTests <= 0)
      None

    val testResults = remainingSampleStats
      .map(second => runTestInternal(firstSampleStats, second, oneSided = true, alpha))

    if (testResults.forall(res => res.contains(TestResult.ExpectedLower)))
      return Some(TestResult.ExpectedLower)

    if (testResults.exists(res => res.isEmpty))
      return None

    Some(TestResult.CantRejectEquality)
  }
}
