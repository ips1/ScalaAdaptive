package adaptivetests.sorting

import adaptivetests.{TestConfiguration, TestSetup}
import adaptivetests.sorttest.SortTest

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.grouping.GroupId
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.api.policies.PauseSelectionAfterStreakPolicy
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.blocks.selection.{LinearRegressionInputBasedStrategy, LoessInterpolationInputBasedStrategy, WindowBoundRegressionInputBasedStrategy, WindowBoundTTestInputBasedStrategy}
import scalaadaptive.core.configuration.blocks.storage.{CachedGroupStorage, CachedRegressionStorage, CachedStatisticsStorage}

/**
  * Created by Petr Kubat on 7/3/17.
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


  private def run(data: Seq[(Int, Array[Int])]): Seq[Result] = {
    import scalaadaptive.api.Implicits._
    val customSort = (
      SortTest.quickSort _ or SortTest.selectionSort
        by (_.length) groupBy (d => GroupId(Math.log(d.length.toDouble).toInt))
        selectUsing Selection.InputBased
        withPolicy new PauseSelectionAfterStreakPolicy(20, 20)
      )

    data.map(in => {
      val combinedTime = measureExecTime(() => customSort(in._2))
      val quickTime = measureExecTime(() => SortTest.quickSort(in._2))
      val selectionTime = measureExecTime(() => SortTest.selectionSort(in._2))
      val lastRecord = customSort.getAnalyticsData.get.getAllRunInfo.last
      val overhead = combinedTime - lastRecord.runTime
      val selectedFun = lastRecord.selectedFunction.toString
      new Result(in._2.length, combinedTime, quickTime, selectionTime)//, overhead, selectedFun)
    })
  }

  private def runTests(setups: Seq[TestSetup], inputs: Seq[(Int, Array[Int])]): Unit = {
    val results = setups.map(s => {
      Adaptive.initialize(s.config)
      val result = run(inputs)
      (s.name, result)
    })

    results.foreach(res => {
      println(s"Results of ${res._1}")
      res._2.sortBy(_.inputSize).foreach(printResult)
    })

    results.foreach(res => {
      println(s"Sum of ${res._1}")
      printSum(res._2)
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

    val setups = List(
      new TestSetup("LOESS", new TestConfiguration
        with LoessInterpolationInputBasedStrategy
        with CachedGroupStorage),
      new TestSetup("LR", new TestConfiguration
        with LinearRegressionInputBasedStrategy
        with CachedRegressionStorage),
      new TestSetup("WBLR", new TestConfiguration
        with WindowBoundRegressionInputBasedStrategy
        with CachedRegressionStorage),
      new TestSetup("WBTT", new TestConfiguration
        with WindowBoundTTestInputBasedStrategy
        with CachedStatisticsStorage)
    )


    // Dummy run to fill caches etc.
    runTests(setups, inputs)

    // Real run
    runTests(setups, inputs)

    val quickSortRes = inputs.map(i => {
      measureExecTime({ () => SortTest.quickSort(i._2) })
    })

    val selectSortRes = inputs.map(i => {
      measureExecTime({ () => SortTest.selectionSort(i._2) })
    })

    println("Quick sort:")
    println(quickSortRes.sum)
    println("Selection sort:")
    println(selectSortRes.sum)
  }
}
