package scalaadaptive.math

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.math.TwoSampleTestResult.TwoSampleTestResult

/**
  * Created by pk250187 on 6/6/17.
  */
trait TwoSampleTTest {
  /**
    * Runs a simple T-Test against two samples with the specified significance level to determine whether the first
    * sample values are from a distribution with significantly higher or lower expectation than the second sample.
    * Possible results:
    * TestResult.HigherExpectation - expecting the first sample to have higher expectation, rejecting the hypothesis
    * that the first sample has lower expectation
    * TestResult.LowerExpectation - expecting the first sample to have lower expectation, rejecting the hypothesis
    * that the first sample has higher expectation
    * TestResult.CantReject - can't reject neither one of the hypothesis
    * @param sampleStats1 First sample data statistics
    * @param sampleStats2 Second sample data statistics
    * @param alpha Significance level of the test
    * @return Test result or None in case of an Error
    */
  def runTest(sampleStats1: StatisticalSummary,
              sampleStats2: StatisticalSummary,
              alpha: Double): Option[TwoSampleTestResult]
}
