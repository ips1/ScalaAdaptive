package adaptivetests.meanbasedtest

import tools.methods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.blocks.{ConsoleLogging, TTestMeanBasedStrategy}
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 6/6/17.
  */
object TTestCantSelect {
  def main(args: Array[String]): Unit = {
    val methods = new TestMethods

    Adaptive.initialize(new DefaultConfiguration with TTestMeanBasedStrategy with ConsoleLogging)

    import scalaadaptive.api.Implicits._
    val function = methods.fastMethod _ or methods.anotherFastMethod or methods.slowMethod selectUsing Selection.MeanBased

    val testRuns = 200
    Seq.range(0, testRuns).foreach(i => {
      println("Running:")
      function(List(1))
    })
  }
}
