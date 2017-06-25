package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.regression.SimpleRegression

/**
  * A wrapper for SimpleRegression that hold the current sum of x values added into the regression. It is required
  * in order to be able to access the mean and to compute the confidence interval for prediction in given point.
  */
class SimpleTestableRegression {
  val innerRegression = new SimpleRegression
  private var sumOfX: Double = 0.0

  def addData(x: Double, y: Double): Unit = {
    sumOfX += x
    innerRegression.addData(x, y)
  }

  private def getQuantile(count: Long, alpha: Double): Double = {
    val degreesOfFreedom = count - 2

    val distribution = new TDistribution(degreesOfFreedom)
    distribution.inverseCumulativeProbability(1 - (alpha / 2))
  }

  private def predictionConfidenceIntervalSize(point: Double, alpha: Double): Double = {
    val n = innerRegression.getN
    val quantile = getQuantile(n, alpha)

    val mse = innerRegression.getMeanSquareError
    val xDevs = innerRegression.getXSumSquares
    val xMean = sumOfX / n
    val pointDev = (point - xMean) * (point - xMean)
    quantile * Math.sqrt(mse * ((1 / n) + (pointDev / xDevs)))
  }

  /**
    * Performs prediction for given point and computes the confidence interval around it with significance level alpha.
    * @param point Point x to predict y for
    * @param alpha Significance level of the confidence interval
    * @return Confidence interval for the prediction with significance alpha
    */
  def predictInterval(point: Double, alpha: Double): (Double, Double) = {
    val predicted = innerRegression.predict(point)
    val intervalSize = predictionConfidenceIntervalSize(point, alpha)
    (predicted - intervalSize, predicted + intervalSize)
  }
}
