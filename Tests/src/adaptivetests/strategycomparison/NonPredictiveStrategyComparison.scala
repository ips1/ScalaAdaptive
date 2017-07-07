package adaptivetests.strategycomparison

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.functions.references.ClosureNameReference

/**
  * Created by pk250187 on 6/24/17.
  */
object NonPredictiveStrategyComparison {
  val methods = new Methods

  def performTest(testData: Iterable[Int], slowerBy: Double): TestRunResult = {
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

    val wrongSelected = fun.getAnalyticsData.get.getAllRunInfo.count(r => r.function match {
      case ClosureNameReference(name) => name == slowerName
      case _ => false
    })

    new TestRunResult(fun.getAnalyticsData.get, wrongSelected)
  }

  def main(args: Array[String]): Unit = {
    val configs = List(
      new BaseLongConfiguration
        with RunTimeMeasurement
        with TTestNonPredictiveStrategy
        with RegressionPredictiveStrategy
        with CachedGroupStorage
        with DefaultHistoryPath
        with BufferedSerialization
        with NoLogging,
      new BaseLongConfiguration
        with RunTimeMeasurement
        with TTestNonPredictiveStrategy
        with LimitedRegressionPredictiveStrategy
        with CachedRegressionAndStatisticsStorage
        with DefaultHistoryPath
        with BufferedSerialization
        with NoLogging,
      new BaseLongConfiguration
        with RunTimeMeasurement
        with TTestNonPredictiveStrategy
        with LoessInterpolationPredictiveStrategy
        with CachedGroupStorage
        with DefaultHistoryPath
        with BufferedSerialization
        with NoLogging
    )

    val testCount = 5
    val runCount = 200
    val minVal = 0
    val maxVal = 500000

    val inputs = Seq.fill(testCount)(Seq.fill(runCount)(Random.nextInt(maxVal - minVal) + minVal).toList)

    configs.foreach(cfg => {
      println("--- NEW CFG ---")
      val results = inputs.map(data => {
        Adaptive.initialize(cfg)
        val res = performTest(data, 1.2)
        println(res.wrongSelected)
        res
      })
    })
  }
}
