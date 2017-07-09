package scalaadaptive.math

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.math.TestResult.TestResult

/**
  * Created by pk250187 on 6/6/17.
  */
trait TTestRunner {
  /**
    * Runs a simple two-sided T-Test with the specified significance level to determine whether the first sample values are
    * from a distribution with higher or lower mean than the second sample.
    * Possible results:
    * TestResult.ExpectedHigher - expecting the first sample to have higher mean, rejecting the hypothesis
    * that the first sample has lower mean
    * TestResult.ExpectedLower - expecting the first sample to have lower mean, rejecting the hypothesis
    * that the first sample has higher mean
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param sampleStats1 First sample data statistics
    * @param sampleStats2 Second sample data statistics
    * @param alpha        Significance level of the test
    * @return Test result or None in case of an Error
    */
  def runTest(sampleStats1: StatisticalSummary,
              sampleStats2: StatisticalSummary,
              alpha: Double): Option[TestResult]

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
  def runTest(firstSampleStats: StatisticalSummary,
               remainingSampleStats: Seq[StatisticalSummary],
               alpha: Double): Option[TestResult]
}