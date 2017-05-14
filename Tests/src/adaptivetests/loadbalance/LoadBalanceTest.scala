package adaptivetests.loadbalance

import scalaadaptive.core.configuration.defaults.{FullHistoryTTestConfiguration, GroupedRunHistoryInterpolationConfiguration}
import scalaadaptive.core.runtime.Adaptive

/**
  * Created by pk250187 on 5/14/17.
  */
object LoadBalanceTest {
  val stageRunCount = 50
  val testController = new LoadBalanceTestController()

  private def runStage() = {
    Seq.range(1, stageRunCount).foreach { j =>
      testController.sendRequest()
    }
  }

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(FullHistoryTTestConfiguration)

    runStage()
    testController.increaseLoad(3123)
    runStage()
    testController.decreaseLoad(3123)
    runStage()
    testController.increaseLoad(3124)
    runStage()
    testController.increaseLoad(3123)
    runStage()
    testController.decreaseLoad(3123)
    runStage()
    testController.decreaseLoad(3124)
  }
}
