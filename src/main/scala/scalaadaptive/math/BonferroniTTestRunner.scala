package scalaadaptive.math
import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.math.TestResult.TestResult

/**
  * Created by pk250187 on 6/6/17.
  */
class BonferroniTTestRunner(val simpleTest: TwoSampleTTest) extends MultipleSampleTTest {
  /**
    * Runs a series of T-Tests to determine whether the first sample is from a distribution with significantly higher
    * or lower mean than all of the other samples.
    * The significance level specified is accumulated for the entire series.
    * Possible results:
    * TestResult.HigherMean - expecting the first sample to have higher mean than all remaining samples,
    * rejecting the hypothesis that the first sample has lower mean than all the remaining samples
    * TestResult.LowerMean - expecting the first sample to have lower mean than all remaining samples,
    * rejecting the hypothesis that the first sample has higher mean than all the remaining samples
    * TestResult.CantReject - can't reject neither one of the hypothesis
    *
    * @param firstSampleStats     First sample data statistics
    * @param remainingSampleStats Sequence of the remaining sample data statistics
    * @param alpha                Significance level of the test
    * @return Test result or None in case of an error
    */
  override def runTests(firstSampleStats: StatisticalSummary,
                        remainingSampleStats: Seq[StatisticalSummary],
                        alpha: Double): Option[TestResult] = {
    // We need to perform one test for each remaining sample
    val numTests = remainingSampleStats.size

    if (numTests <= 0)
      None

    val testResults = remainingSampleStats
      .map(second => simpleTest.runTest(firstSampleStats, second, alpha))

    if (testResults.forall(res => res.contains(TestResult.ExpectedHigher)))
      return Some(TestResult.ExpectedHigher)

    if (testResults.forall(res => res.contains(TestResult.ExpectedLower)))
      return Some(TestResult.ExpectedLower)

    if (testResults.exists(res => res.isEmpty))
      return None

    Some(TestResult.CantRejectEquality)
  }
}
