package adaptivetests.ttesttest

import adaptivetests.testmethods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Selection
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration

/**
  * Created by pk250187 on 6/6/17.
  */
object TTestCantSelect {
  def main(args: Array[String]): Unit = {
    val methods = new TestMethods

    Adaptive.initialize(new FullHistoryTTestConfiguration)

    import scalaadaptive.api.Implicits._
    val function = methods.fastMethod _ or methods.anotherFastMethod or methods.slowMethod selectUsing Selection.Discrete

    val testRuns = 200
    Seq.range(0, testRuns).foreach(i => {
      println("Running:")
      function(List(1))
    })
  }
}
