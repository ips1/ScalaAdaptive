package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.StatisticalSummary
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.logging.Logger

/**
  * Created by pk250187 on 5/2/17.
  */
class TTestRunner(val getLogger: () => Logger) {
  def getQuantile(count1: Long, count2: Long, alpha: Double): Double = {
    val degreesOfFreedom = count1 + count2 - 2

    val distribution = new TDistribution(degreesOfFreedom)
    distribution.inverseCumulativeProbability(1 - alpha)
  }

  def runTest(sampleStats1: StatisticalSummary,
              sampleStats2: StatisticalSummary,
              alpha: Double): Option[TestResult] = {

    // TODO: Chceck alpha

    val tValue = TestUtils.t(sampleStats1, sampleStats2)
    val quantile = getQuantile(sampleStats1.getN, sampleStats2.getN, alpha)

    getLogger().log("Performing test")
    getLogger().log(s"T-value = $tValue")
    getLogger().log(s"Quantile = $quantile")

    if (tValue > quantile) {
      return Some(HigherExpectation())
    }
    else if (tValue < -1 * quantile) {
      return Some(LowerExpectation())
    }

    getLogger().log(s"Can't reject")

    Some(CantRejectEquality())
  }
}
