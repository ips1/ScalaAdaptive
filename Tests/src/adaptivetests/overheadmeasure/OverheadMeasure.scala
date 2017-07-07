package adaptivetests.overheadmeasure

import adaptivetests.sorting.Sorter

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.api.policies.StopSelectingWhenDecidedPolicy
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 7/1/17.
  */
object OverheadMeasure {
  val runCount = 5000
  val maxDataSize = 10000

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

  def main(args: Array[String]): Unit = {

    val sorter = new Sorter()

    val config = new BaseLongConfiguration
      with RunTimeMeasurement
      with TTestNonPredictiveStrategy
      //with LimitedRegressionPredictiveStrategy
      with RegressionPredictiveStrategy
      //with LoessInterpolationPredictiveStrategy
      //with CachedRegressionAndStatisticsStorage
      //with CachedGroupStorage
      with DefaultHistoryPath
      with BufferedSerialization
      with NoLogging

    Adaptive.initialize(config)

    import scalaadaptive.api.Implicits._
    val customSort = sorter.quickSort _ or sorter.selectionSort by (_.size) storeUsing Storage.Global selectUsing Selection.Predictive

    val inputs = Seq.range(0, runCount).map(i => {
      val size = Random.nextInt(maxDataSize)
      (i, Seq.fill(size)(Random.nextInt).toList)
    })

    val results = inputs.map(in => (in._1, performTestStep(in._2, customSort)))

    results.foreach(r => {
      if (r._1 % 50 == 0) {
        println(s"${r._1}, ${r._2.functionTime}, ${r._2.selectOverhead}, ${r._2.policyOverhead}")
      }
    })
  }
}
