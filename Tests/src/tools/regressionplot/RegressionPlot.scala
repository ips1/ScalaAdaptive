package tools.regressionplot

import adaptivetests.sorttest.SortTest
import org.apache.commons.math3.stat.regression
import org.apache.commons.math3.stat.regression.SimpleRegression
import tools.wait.WaitMethods

import scala.util.Random
import scalaadaptive.math.{RegressionTTestResult, RegressionTTestRunner}

/**
  * Created by pk250187 on 6/10/17.
  */
object RegressionPlot {
  def quadraticFunctionWithError(size: Int): Unit = {
    val randomMultiplier = (Random.nextDouble() * 0.1) + 1
    WaitMethods.waitForNanos((size * size * 10 * randomMultiplier).toInt)
  }

  def createRandomArray(): Array[Int] = {
    Seq.fill(1)(Random.nextInt).toArray
  }

  def main(args: Array[String]): Unit = {
    val data = Seq.fill(200)(Random.nextInt).toArray
    SortTest.selectionSort(data)
    SortTest.selectionSort(data)
    SortTest.selectionSort(data)

    val sampleCount = 1
    var cantRejectCount = 0

    val min = 0
    val max = 40000

    val count = 200

    Seq.range(0, sampleCount).foreach(j => {
      val regression = new SimpleRegression

      Seq.range(0, count).foreach(i => {
        val inputSize = Random.nextInt(max - min) + min
        val data = Seq.fill(inputSize)(Random.nextInt).toArray
        val startTime: Long = System.nanoTime
        //Seq.fill(inputSize)(createRandomArray()).toList
        SortTest.selectionSort(data)
        val durationNanos: Long = System.nanoTime - startTime
        val duration: Double = durationNanos.toDouble / (1000 * 1000)
        regression.addData(inputSize.toDouble, duration)
        println(s"$inputSize,$duration")
      })

      val testRunner = new RegressionTTestRunner
      val alpha = 0.05

      val result = testRunner.runTest(regression, alpha)

      println(regression.getRSquare)
      println(regression.getR)

      if (result.contains(RegressionTTestResult.CantRejectNoRelationship)) {
        cantRejectCount += 1
      }
    })

    println(s"$cantRejectCount / $sampleCount : ${(cantRejectCount.toDouble / sampleCount) * 100}")

//    var alpha: Double = 0.05
//    while(alpha <= 1.0) {
//      val result = testRunner.runTest(regression, alpha)
//      println(s"$alpha: $result")
//      alpha += 0.05
//    }
  }
}
