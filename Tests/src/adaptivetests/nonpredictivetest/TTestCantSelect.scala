package adaptivetests.nonpredictivetest

import tools.methods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.blocks.{ConsoleLogging, TTestNonPredictiveStrategy}
import scalaadaptive.core.configuration.defaults.{DefaultConfiguration, FullHistoryTTestConfiguration}

/**
  * Created by pk250187 on 6/6/17.
  */
object TTestCantSelect {
  def main(args: Array[String]): Unit = {
    val methods = new TestMethods

    Adaptive.initialize(new DefaultConfiguration with TTestNonPredictiveStrategy with ConsoleLogging)

    import scalaadaptive.api.Implicits._
    val function = methods.fastMethod _ or methods.anotherFastMethod or methods.slowMethod selectUsing Selection.NonPredictive

    val testRuns = 200
    Seq.range(0, testRuns).foreach(i => {
      println("Running:")
      function(List(1))
    })
  }
}
