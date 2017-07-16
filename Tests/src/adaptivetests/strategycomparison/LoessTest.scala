package adaptivetests.strategycomparison

import java.io.PrintWriter

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.identifiers.ClosureIdentifier
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.selection.{LeastDataSelectionStrategy, LoessInterpolationSelectionStrategy, LowRunAwareSelectionStrategy, SelectionStrategy}

/**
  * Created by Petr Kubat on 6/24/17.
  */
object LoessTest {
  val methods = new Methods

  def performTest(testData: Iterable[Int], slowerBy: Double): TestRunResult = {
    val slowerFun = methods.createSlowerLinear(slowerBy)
    val normalFun = methods.createNormalLinear

    val slowerName = slowerFun.getClass.getTypeName

    import scalaadaptive.api.Implicits._
    val fun = slowerFun or normalFun by (i => i) selectUsing Selection.InputBased

    testData.foreach(n => {
      val startTime = System.nanoTime
      val res = fun(n)
      val duration = System.nanoTime - startTime
    })

    val wrongSelected = fun.getAnalyticsData.get.getAllRunInfo.count(r => r.selectedFunction match {
      case ClosureIdentifier(name) => name == slowerName
      case _ => false
    })

    if (wrongSelected > 100) {
      fun.getAnalyticsData.get.saveData(new PrintWriter(System.out))
    }

    new TestRunResult(fun.getAnalyticsData.get, wrongSelected)
  }

  abstract class ComparisonConfiguration extends BaseLongConfiguration
    with RunTimeMeasurement
    with TTestMeanBasedStrategy
    with CachedGroupStorage
    with DefaultHistoryPath
    with BufferedSerialization
    with NoLogging

  def main(args: Array[String]): Unit = {
    val configs = List(
      new ComparisonConfiguration
        with LoessInterpolationInputBasedStrategy
    )

    val errorFactors = List(1.0, 3.0)

    val testCount = 50
    val runCount = 200
    val minVal = 100000
    val maxVal = 500000

    val inputs = Seq.fill(testCount)(Seq.fill(runCount)(Random.nextInt(maxVal - minVal) + minVal).toList)

    val resByError = errorFactors.map(err => {
      println(s"--- NEW ERROR: $err ---")
      val resByError = configs.map(cfg => {
        println("--- NEW CFG ---")
        val results = inputs.map(data => {
          Adaptive.initialize(cfg)
          val res = performTest(data, err)
          println(res.wrongSelected)
          res
        })

        results.map(_.wrongSelected)
      })
      resByError
    })

    Seq.range(0, testCount).foreach(i => {
      val lineParts = resByError.map(byErr => {
        val innerResults = byErr.map(l => l(i))
        innerResults.mkString(",")
      })
      val line = lineParts.mkString(",,")
      println(line)
    })
  }
}
