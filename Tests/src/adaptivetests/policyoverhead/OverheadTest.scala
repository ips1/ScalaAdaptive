package adaptivetests.policyoverhead

import adaptivetests.testmethods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.api.options.Storage
import scalaadaptive.core.configuration.blocks.NoLogger
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.policies.{AlwaysUseLastPolicy, StopSelectingWhenDecidedPolicy}

/**
  * Created by pk250187 on 6/5/17.
  */
object OverheadTest {
  val methods = new TestMethods()
  val utils = new OverheadUtils()

  def run(): Unit = {
    Adaptive.reset()

    import scalaadaptive.api.Implicits._
    val noPolicyPersistent = methods.fastMethod _ or methods.fastMethod storeUsing Storage.Persistent

    val noPolicy = methods.fastMethod _ or methods.fastMethod

    val stopSelectingFunction = methods.fastMethod _ or methods.fastMethod withPolicy new StopSelectingWhenDecidedPolicy(20)

    val useLastFunction = methods.fastMethod _ or methods.fastMethod withPolicy new AlwaysUseLastPolicy
    // Last should be the first method if no statistics are present

    val runCount = 200

    // First run a couple of time to initialize everything
    Seq.range(0, 50).foreach(i => noPolicy(List(1)))

    println("No policy persistent:")
    utils.performTest(runCount, List(1), methods.fastMethod, noPolicyPersistent)
    println("No policy:")
    utils.performTest(runCount, List(1), methods.fastMethod, noPolicy)
    println("StopSelectingWhenDecided:")
    utils.performTest(runCount, List(1), methods.fastMethod, stopSelectingFunction)
    println("AlwaysUseLastPolicy:")
    utils.performTest(runCount, List(1), methods.fastMethod, useLastFunction)
  }

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new FullHistoryTTestConfiguration() with NoLogger)
    Seq.range(0, 1).foreach(i => run())
  }
}
