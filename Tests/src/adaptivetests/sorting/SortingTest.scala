package adaptivetests.sorting

import adaptivetests.sorttest.SortTest
import adaptivetests.strategycomparison.TestConfiguration

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.api.policies.PauseSelectionAfterStreakPolicy
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 7/3/17.
  */
object SortingTest {
  val runCount = 500
  val maxDataSize = 5000

  private def measureExecTime(fun: () => Unit): Long = {
    val start = System.nanoTime
    fun()
    System.nanoTime - start
  }

  class Result(val inputSize: Int,
               val combinedTime: Long,
               val quickTime: Long,
               val selectionTime: Long)

  private def initRegression() = {
    val config = new TestConfiguration
      with LinearRegressionInputBasedStrategy
      with CachedRegressionAndStatisticsStorage
    Adaptive.initialize(config)
  }

  private def initLimitedRegression() = {
    val config = new TestConfiguration
      with WindowBoundRegressionInputBasedStrategy
      with CachedRegressionAndStatisticsStorage
    Adaptive.initialize(config)
  }

  private def initLoess() = {
    val config = new TestConfiguration
      with LoessInterpolationInputBasedStrategy
      with CachedGroupStorage
    Adaptive.initialize(config)
  }


  private def run(data: Seq[(Int, Array[Int])]): Seq[Result] = {
    import scalaadaptive.api.Implicits._
    val customSort = (
      SortTest.quickSort _ or SortTest.selectionSort
        by (_.length) groupBy (d => GroupId(Math.log(d.length.toDouble).toInt))
        selectUsing Selection.Predictive withPolicy new PauseSelectionAfterStreakPolicy(20, 20)
      )

    data.map(in => {
      val combinedTime = measureExecTime(() => customSort(in._2))
      val quickTime = measureExecTime(() => SortTest.quickSort(in._2))
      val selectionTime = measureExecTime(() => SortTest.selectionSort(in._2))
      val lastRecord = customSort.getAnalyticsData.get.getAllRunInfo.last
      val overhead = combinedTime - lastRecord.runTime
      val selectedFun = lastRecord.function.toString
      new Result(in._2.length, combinedTime, quickTime, selectionTime)//, overhead, selectedFun)
    })
  }

  private def printResult(res: Result) =
    println(s"${res.inputSize}, ${res.combinedTime}, ${res.quickTime}, ${res.selectionTime}")

  private def printSum(res: Seq[Result]) =
    println(s"${res.map(_.combinedTime).sum}, ${res.map(_.quickTime).sum}, ${res.map(_.selectionTime).sum}")

  def main(args: Array[String]): Unit = {

    val inputs = Seq.range(0, runCount).map(i => {
      val size = Random.nextInt(maxDataSize)
      (i, Seq.fill(size)(Random.nextInt).toArray)
    })

    // Dummy run to fill caches etc.
    initLoess()
    run(inputs)

    initLoess()
    val resultsLoess = run(inputs)
    initRegression()
    val resultsRegression = run(inputs)
    initLimitedRegression()
    val resultsLimitedRegression = run(inputs)

    resultsRegression.sortBy(_.inputSize).foreach(printResult)
    resultsLimitedRegression.sortBy(_.inputSize).foreach(printResult)
    resultsLoess.sortBy(_.inputSize).foreach(printResult)

    println("Loess sums:")
    printSum(resultsLoess)
    println("Regression sums:")
    printSum(resultsRegression)
    println("Limited regression sums:")
    printSum(resultsLimitedRegression)
  }
}
