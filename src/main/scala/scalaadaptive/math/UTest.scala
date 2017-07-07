package scalaadaptive.math

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.math.TestResult.TestResult

/**
  * Created by pk250187 on 7/7/17.
  */
trait UTest {
  /**
    * Runs a simple U-Test with the specified significance level to determine whether the first sample values are
    * from a distribution with higher mean than the second sample.
    * Possible results:
    * TestResult.HigherMean - expecting the first sample to have higher mean, rejecting the hypothesis
    * that the first sample has lower mean
    * TestResult.LowerMean - expecting the first sample to have lower mean, rejecting the hypothesis
    * that the first sample has higher mean
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param sample1 First sample values and statistical summary
    * @param sample2 Second sample values and statistical summary
    * @param alpha        Significance level of the test
    * @return Test result or None in case of an Error
    */
  def runTest(sample1: (Iterable[Double], StatisticalSummary),
              sample2: (Iterable[Double], StatisticalSummary),
              alpha: Double): Option[TestResult]

  /**
    * Runs a series of U-tests to determine whether the first sample is from a distribution with significantly higher
    * or lower mean than all of the other samples.
    * The significance level specified is accumulated for the entire series.
    * Possible results:
    * TestResult.HigherMean - expecting the first sample to have higher mean than all remaining samples,
    * rejecting the hypothesis that the first sample has lower mean than all the remaining samples
    * TestResult.LowerMean - expecting the first sample to have lower mean than all remaining samples,
    * rejecting the hypothesis that the first sample has higher mean than all the remaining samples
    * TestResult.CantReject - can't reject neither one of the hypothesis
    */
  def runTest(firstSample: (Iterable[Double], StatisticalSummary),
              otherSamples: Iterable[(Iterable[Double], StatisticalSummary)],
              alpha: Double): Option[TestResult]
}
