package adaptivetests.regressionselection

import adaptivetests.TestRunner
import adaptivetests.sorting.Sorter
import adaptivetests.testmethods.TestMethods

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.{LimitedRegressionSelection, NoGrouping}
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.runtime.selection.LimitedRegressionSelector

/**
  * Created by pk250187 on 6/7/17.
  */
object RegressionSelectionTest {
  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new FullHistoryTTestConfiguration with LimitedRegressionSelection with NoGrouping)
    val testMethods = new TestMethods()
    //val runner = new TestRunner()
    //runner.runIncrementalTest(l => testMethods.functionContinuous(l))

    val testCount = 100
    val maxSize = 200
    val random = new Random(System.nanoTime)

    Seq.range(0, testCount).foreach(i => {
      val inputSize = random.nextInt(maxSize)
      val data = Seq.fill(inputSize)(Random.nextInt).toList
      testMethods.functionContinuous(data)
    })
  }
}
