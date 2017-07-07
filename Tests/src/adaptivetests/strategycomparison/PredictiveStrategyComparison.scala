package adaptivetests.strategycomparison

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.references.ClosureNameReference
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.{LeastDataSelectionStrategy, LoessInterpolationSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}

/**
  * Created by pk250187 on 6/24/17.
  */
object PredictiveStrategyComparison {
  val methods = new Methods

  def performTest(testData: Iterable[Int], slowerBy: Double): Int = {
    val slowerFun = methods.createSlower(slowerBy)
    val normalFun = methods.createNormal

    val slowerName = slowerFun.getClass.getTypeName

    import scalaadaptive.api.Implicits._
    val fun = slowerFun or normalFun by (i => i) selectUsing Selection.Predictive

    testData.foreach(n => {
      val startTime = System.nanoTime
      val res = fun(n)
      val duration = System.nanoTime - startTime
    })

    fun.getAnalyticsData.get.getAllRunInfo.count(r => r.function match {
      case ClosureNameReference(name) => name == slowerName
      case _ => false
    })
  }

  def main(args: Array[String]): Unit = {
    val configs = List(new BaseLongConfiguration
      with RunTimeMeasurement
      with TTestNonPredictiveStrategy
      //with LimitedRegressionPredictiveStrategy
      with RegressionPredictiveStrategy
      //with LoessInterpolationPredictiveStrategy
      //with CachedRegressionAndStatisticsStorage
      with CachedGroupStorage
      with DefaultHistoryPath
      with BufferedSerialization
      with NoLogging,
      new BaseLongConfiguration
        with RunTimeMeasurement
        with TTestNonPredictiveStrategy
        with LimitedRegressionPredictiveStrategy
        //with RegressionPredictiveStrategy
        //with LoessInterpolationPredictiveStrategy
        with CachedRegressionAndStatisticsStorage
        //with CachedGroupStorage
        with DefaultHistoryPath
        with BufferedSerialization
        with NoLogging,
      new BaseLongConfiguration
        with RunTimeMeasurement
        with TTestNonPredictiveStrategy
        //with LimitedRegressionPredictiveStrategy
        //with RegressionPredictiveStrategy
        with LoessInterpolationPredictiveStrategy
        //with CachedRegressionAndStatisticsStorage
        with CachedGroupStorage
        with DefaultHistoryPath
        with BufferedSerialization
        with NoLogging
    )

    val testCount = 5

    Seq.range(0, testCount).foreach(i => {
      val runCount = 200
      val data = Seq.fill(runCount)(Random.nextInt(500000)).toList

      val results = configs.map(cfg => {
        Adaptive.initialize(cfg)
        performTest(data, 0.2)
      })

      results.foreach(println)
    })
  }
}
