package scalaadaptive.math

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.regression.SimpleRegression

import scala.collection.mutable

/**
  * A wrapper for SimpleRegression that hold the current sum of x values added into the regression. It is required
  * in order to be able to access the mean and to compute the confidence interval for prediction in given point.
  *
  * In addition, it analyzes the different values of x and is able to tell if there are more than the minimum
  * (3 by default).
  */
class SimpleTestableRegression {
  import scalaadaptive.extensions.DoubleExtensions._

  val innerRegression = new SimpleRegression
  val minXCount = 3

  private var enoughX: Boolean = false
  private var sumOfX: Double = 0.0
  private val differentX: mutable.Set[Double] = new mutable.HashSet[Double]()

  /**
    * Finds out whether the regression has enough samples with different X values. The default minimum is 3.
    * @return True if the regression has enough samples, false otherwise.
    */
  def hasEnoughX: Boolean = enoughX

  /**
    * Adds a new sample to the regression.
    * @param x X value of the sample
    * @param y Y value of the sample
    */
  def addData(x: Double, y: Double): Unit = {
    sumOfX += x
    if (!enoughX) {
      differentX.add(x)
      enoughX = differentX.size >= minXCount
    }
    innerRegression.addData(x, y)
  }

  private def getQuantile(count: Long, alpha: Double): Double = {
    val degreesOfFreedom = count - 2

    val distribution = new TDistribution(degreesOfFreedom)
    distribution.inverseCumulativeProbability(1 - (alpha / 2))
  }

  private def predictionConfidenceIntervalSize(point: Double, alpha: Double): Option[Double] = {
    if (!enoughX) {
      return None
    }

    val n = innerRegression.getN
    val quantile = getQuantile(n, alpha)

    val mse = innerRegression.getMeanSquareError
    val xDevs = innerRegression.getXSumSquares

    if (n == 0 || mse.isNaN || xDevs.isNaN) {
      return None
    }

    val xMean = sumOfX / n
    val pointDev = (point - xMean) * (point - xMean)
    Some(quantile * Math.sqrt(mse * ((1 / n) + (pointDev / xDevs))))
  }

  /**
    * Safe wrapper for the SimpleRegression.predict() method, which can return NaN in case of failrue
    * @param point The point we want the prediction for
    * @return Either the predicted value or None in case the prediction cannot be done
    */
  def predict(point: Double): Option[Double] = {
    val result = innerRegression.predict(point)
    result.asOption
  }

  /**
    * Performs prediction for given point and computes the confidence interval around it with significance level alpha.
    * @param point Point x to predict y for
    * @param alpha Significance level of the confidence interval
    * @return Confidence interval for the prediction with significance alpha
    */
  def predictInterval(point: Double, alpha: Double): Option[(Double, Double)] =
    for {
      predicted <- predict(point)
      intervalSize <- predictionConfidenceIntervalSize(point, alpha)
    } yield (predicted - intervalSize, predicted + intervalSize)
}
