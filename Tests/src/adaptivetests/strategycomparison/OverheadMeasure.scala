package adaptivetests.strategycomparison

import adaptivetests.TestConfiguration
import adaptivetests.sorting.Sorter

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.core.configuration.{BaseLongConfiguration, Configuration}
import scalaadaptive.core.configuration.blocks._

/**
  * Created by Petr Kubat on 7/1/17.
  */
object OverheadMeasure {
  val runCount = 20000
  val maxDataSize = 2000

  class RunMeasure(val totalTime: Long, val functionTime: Long, val selectOverhead: Long) {
    val totalOverhead: Long = totalTime - functionTime
    val policyOverhead: Long = totalOverhead - selectOverhead
  }

  private def measureExecTime(fun: () => Unit): Long = {
    val start = System.nanoTime
    fun()
    System.nanoTime - start
  }

  private def performTestStep(input: List[Int], sort: MultiFunction1[List[Int], List[Int]]): RunMeasure = {
    val externalTime = measureExecTime(() => sort(input))
    val lastRecord = sort.getAnalyticsData.get.getAllRunInfo.last
    new RunMeasure(externalTime, lastRecord.runTime, lastRecord.overheadTime)
  }

  def runTest(conf: (Configuration, Selection)): (Seq[String], Double) = {
    Adaptive.initialize(conf._1)
    val sorter = new Sorter()


    import scalaadaptive.api.Implicits._
    val customSort = sorter.quickSort _ or sorter.selectionSort by (_.size) storeUsing Storage.Global selectUsing conf._2

    val inputs = Seq.range(0, runCount).map(i => {
      val size = Random.nextInt(maxDataSize)
      (i, Seq.fill(size)(Random.nextInt).toList)
    })

    val results = inputs.map(in => (in._1, performTestStep(in._2, customSort)))

    val toPrint = results
      .filter(r => r._1 % 50 == 0)
      .map(r => s"${r._1}, ${r._2.functionTime}, ${r._2.selectOverhead}, ${r._2.policyOverhead}")



    val totalOverhead = results.map(_._2.selectOverhead).sum
    val avgOverhead = (totalOverhead.toDouble / results.size) / (1000 * 1000)

    (toPrint, avgOverhead)
  }

  def main(args: Array[String]): Unit = {
    val configs = List(
      (new TestConfiguration
      with TTestMeanBasedStrategy
      with CachedRegressionAndStatisticsStorage, Selection.MeanBased),
      (new TestConfiguration
        with TTestMeanBasedStrategy, Selection.MeanBased),
      (new TestConfiguration
        with UTestMeanBasedStrategy, Selection.MeanBased),
      (new TestConfiguration
        with LinearRegressionInputBasedStrategy
        with CachedRegressionAndStatisticsStorage, Selection.InputBased),
      (new TestConfiguration
        with LinearRegressionInputBasedStrategy, Selection.InputBased),
      (new TestConfiguration
        with WindowBoundRegressionInputBasedStrategy, Selection.InputBased),
      (new TestConfiguration
        with WindowBoundTTestInputBasedStrategy, Selection.InputBased),
      (new TestConfiguration
        with LoessInterpolationInputBasedStrategy, Selection.InputBased)
    )

    val results = configs.map(cfg => runTest(cfg))

    val strings = results.map(_._1).toArray

    val recordCount = strings.head.size

    Seq.range(0, recordCount).foreach(i => {
      val lineParts = strings.map(s => s(i))
      val line = lineParts.mkString(",,")
      println(line)
    })

    println()

    val avgs = results.map(_._2.toString).mkString(",")
    println(avgs)
  }
}
