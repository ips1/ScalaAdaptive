package scalaadaptive.math
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.inference.{MannWhitneyUTest, TTest}

import scalaadaptive.core.logging.Logger
import scalaadaptive.math.TestResult.TestResult

/**
  * Created by Petr Kubat on 7/7/17.
  *
  * An implementation of [[UTestRunner]] that uses the default Mann-Whitney U-test.
  * For more information see the original thesis text.
  *
  */
class MannWhitneyUTestRunner(val logger: Logger) extends UTestRunner {
  private def runTestInternal(sample1: (Iterable[Double], StatisticalSummary),
                              sample2: (Iterable[Double], StatisticalSummary),
                              oneSided: Boolean,
                              alpha: Double): Option[TestResult] = {
    if (alpha < 0.0 || alpha > 1.0) {
      logger.log(s"Invalid alpha provided to UTestRunner: $alpha")
      return None
    }

    logger.log(s"Performing U-test on significance level $alpha")

    val test = new MannWhitneyUTest()

    val pValue = try {
      test.mannWhitneyUTest(sample1._1.toArray, sample2._1.toArray)
    } catch {
      case e: Exception =>
        logger.log(s"Exception during t-test: $e")
        return None
    }

    // We need to use 2 * alpha for one-sided testing
    val realAlpha = if (oneSided) alpha * 2 else alpha
    if (pValue > realAlpha) {
      // Can't decide about the one-sided alternative
      logger.log(s"Can't reject")
      return Some(TestResult.CantRejectEquality)
    }

    // Equality was rejected, we can accept the one-sided alternative
    if (sample1._2.getMean > sample2._2.getMean) {
      logger.log(s"Success with ExpectedHigher result")
      Some(TestResult.ExpectedHigher)
    }
    else {
      logger.log(s"Success with ExpectedLower result")
      Some(TestResult.ExpectedLower)
    }
  }

  /**
    * Runs a simple two-sided U-Test with the specified significance level to determine whether the first sample values are
    * from a distribution with higher or lower mean than the second sample.
    * Possible results:
    * TestResult.ExpectedHigher - expecting the first sample to have higher mean, rejecting the hypothesis
    * that the first sample has lower mean
    * TestResult.ExpectedLower - expecting the first sample to have lower mean, rejecting the hypothesis
    * that the first sample has higher mean
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param sample1 First sample values and statistical summary
    * @param sample2 Second sample values and statistical summary
    * @param alpha        Significance level of the test
    * @return Test result or None in case of an Error
    */
  override def runTest(sample1: (Iterable[Double], StatisticalSummary),
                       sample2: (Iterable[Double], StatisticalSummary),
                       alpha: Double): Option[TestResult] =
    runTestInternal(sample1, sample2, oneSided = false, alpha)

  /**
    * Runs a series of U-tests to determine whether the first sample is from a distribution with significantly
    * lower mean than all of the other samples. The test is one-sided!
    * The significance level specified is accumulated for the entire series.
    * Possible results:
    * TestResult.ExpectedLower - expecting the first sample to have lower mean than all remaining samples,
    * rejecting the hypothesis that the first sample has higher mean than all the remaining samples
    * TestResult.CantReject - can't reject neither one of the hypothesis
    */
  override def runTest(firstSample: (Iterable[Double], StatisticalSummary),
                       otherSamples: Iterable[(Iterable[Double], StatisticalSummary)],
                       alpha: Double): Option[TestResult] = {
    // We need to perform one test for each remaining sample
    val numTests = otherSamples.size

    if (numTests <= 0)
      None

    val testResults = otherSamples
      .map(second => runTestInternal(firstSample, second, oneSided = true, alpha))

    if (testResults.forall(res => res.contains(TestResult.ExpectedLower)))
      return Some(TestResult.ExpectedLower)

    if (testResults.exists(res => res.isEmpty))
      return None

    Some(TestResult.CantRejectEquality)
  }
}
