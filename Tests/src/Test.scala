import adaptivetests.TestRunner
import adaptivetests.sorting.Sorter
import adaptivetests.testmethods.TestMethods

import scala.util.Random
import scalaadaptive.core.configuration.defaults._
import scalaadaptive.core.runtime.Adaptive

/**
  * Created by pk250187 on 3/19/17.
  */
object Test {
  def main(args: Array[String]): Unit = {

    val runner = new TestRunner(4)

    //runTest(l => l.sort())
    //runTest(l => sorter.sort(l))
//    val configurations = List(ImmutableFullHistoryInterpolationConfiguration)//, GroupedRunHistoryInterpolationConfiguration)

    //val configurations = List(ImmutableFullHistoryInterpolationConfiguration)
    //val configurations = List(GroupedRunHistoryInterpolationConfiguration)
    val configurations = List(FullHistoryTTestConfiguration)
    configurations.foreach(cfg => {
      Adaptive.initialize(cfg)
      val sorter = new Sorter()
      val testMethods = new TestMethods()
      //runner.runIncrementalTest(l => sorter.sort(l))
      //runner.runIncrementalTest(l => testMethods.functionContinuous(l))
      runner.runIncrementalTest(l => testMethods.functionContinuousWithLimitedOverhead(l))
      //runner.runTest(l => sorter.sort(l))
      //runner.runTest(l => testMethods.function(l))
    })
  }
}
