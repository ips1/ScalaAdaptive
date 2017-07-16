package adaptivetests.selectionsetting

import java.time.Duration

import adaptivetests.TestRunner
import tools.methods.TestMethods

import scalaadaptive.core.configuration.defaults.{FullHistoryTTestConfiguration, GroupedRunHistoryInterpolationConfiguration}
import scalaadaptive.api.options.Selection
import scalaadaptive.api.Adaptive

/**
  * Created by Petr Kubat on 5/8/17.
  */
object SelectionSettingTest {
  import scalaadaptive.api.Implicits._

  val testMethods = new TestMethods()
  val discreteFunc = testMethods.slowMethod _ or testMethods.fastMethod selectUsing Selection.MeanBased
  val continuousFunc = testMethods.linearHighConstant _ or testMethods.quadraticMinConstant by { _.size } selectUsing Selection.InputBased

  val runner = new TestRunner()

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new GroupedRunHistoryInterpolationConfiguration)

    runner.runIncrementalTest(l => discreteFunc(l))
    runner.runIncrementalTest(l => continuousFunc(l))
  }
}