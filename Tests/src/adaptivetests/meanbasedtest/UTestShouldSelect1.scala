package adaptivetests.meanbasedtest

import tools.methods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.blocks.{ConsoleLogging, UTestMeanBasedStrategy}
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 7/8/17.
  */
object UTestShouldSelect1 {
  def main(args: Array[String]): Unit = {
    val methods = new TestMethods

    Adaptive.initialize(new DefaultConfiguration with UTestMeanBasedStrategy with ConsoleLogging)

    import scalaadaptive.api.Implicits._
    val function = methods.fastMethod _ or methods.slowMethod or methods.slowestMethod selectUsing Selection.MeanBased

    val testRuns = 200
    Seq.range(0, testRuns).foreach(i => {
      println("Running:")
      function(List(1))
    })
  }
}
