package adaptivetests.regressionselection

import java.io.PrintWriter

import adaptivetests.TestRunner
import adaptivetests.sorting.Sorter
import adaptivetests.sorttest.SortTest
import tools.methods.TestMethods

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.defaults.{FullHistoryRegressionConfiguration, FullHistoryTTestConfiguration}
import scalaadaptive.core.runtime.selection.strategies.RegressionSelectionStrategy

/**
  * Created by Petr Kubat on 6/7/17.
  */
object RegressionSelectionTest {
  def runTest(data: Seq[Array[Int]]): Unit = {
    Adaptive.initialize(new FullHistoryRegressionConfiguration with LinearRegressionInputBasedStrategy with NoLogging)
    val testMethods = new TestMethods()
    //val runner = new TestRunner()
    //runner.runIncrementalTest(l => testMethods.functionContinuous(l))

    //val testCount = 200
    val maxSize = 5000
    val random = new Random(System.nanoTime)

    data.foreach(d => {
      val inputSize = random.nextInt(maxSize)
      //val data = Seq.fill(inputSize)(Random.nextInt).toList
      //testMethods.functionContinuous(data)
      SortTest.sort(d)
    })

    SortTest.sort.getAnalyticsData.foreach(d => d.saveData(new PrintWriter(System.out)))

  }

  def main(args: Array[String]): Unit = {

  }
}
