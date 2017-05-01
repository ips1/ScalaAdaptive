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
    import Sortable._

    val runner = new TestRunner()

    //runTest(l => l.sort())
    val sorter = new Sorter()
    val testMethods = new TestMethods()
    //runTest(l => sorter.sort(l))
//    val configurations = List(ImmutableFullHistoryInterpolationConfiguration)//, GroupedRunHistoryInterpolationConfiguration)

    //val configurations = List(ImmutableFullHistoryInterpolationConfiguration)
    val configurations = List(GroupedRunHistoryInterpolationConfiguration)
    configurations.foreach(cfg => {
      Adaptive.initialize(cfg)
      runner.runIncrementalTest(l => testMethods.function(l))
    })
  }
}
