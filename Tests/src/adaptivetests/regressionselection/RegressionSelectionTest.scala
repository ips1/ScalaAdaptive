package adaptivetests.regressionselection

import java.io.PrintWriter

import adaptivetests.TestRunner
import adaptivetests.sorting.Sorter
import adaptivetests.sorttest.SortTest
import tools.methods.TestMethods

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.{InterpolationSelection, LimitedRegressionSelection, NoGrouping, NoLogger}
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.runtime.selection.LimitedRegressionSelectionStategy

/**
  * Created by pk250187 on 6/7/17.
  */
object RegressionSelectionTest {
  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new FullHistoryTTestConfiguration with LimitedRegressionSelection with NoGrouping)
    val testMethods = new TestMethods()
    //val runner = new TestRunner()
    //runner.runIncrementalTest(l => testMethods.functionContinuous(l))

    val testCount = 1000
    val maxSize = 5000
    val random = new Random(System.nanoTime)

    Seq.range(0, testCount).foreach(i => {
      val inputSize = random.nextInt(maxSize)
      val data = Seq.fill(inputSize)(Random.nextInt).toList
      //testMethods.functionContinuous(data)
      SortTest.sort(data.toArray)
    })

    SortTest.sort.getAnalyticsData.foreach(d => d.saveData(new PrintWriter(System.out)))
  }
}
