package adaptivetests.limited

import java.time.Duration

import adaptivetests.TestRunner
import tools.methods.TestMethods

import scalaadaptive.core.configuration.defaults.DefaultConfiguration
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.CachedStatisticsStorage

/**
  * Created by Petr Kubat on 5/8/17.
  */
object LimitedTest {
  import scalaadaptive.api.Implicits._

  val testMethods = new TestMethods()
  val limitedFunc = testMethods.slowMethod _ or testMethods.fastMethod limitedTo Duration.ofSeconds(5)

  val runner = new TestRunner()

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new DefaultConfiguration with CachedStatisticsStorage)

    runner.runIncrementalTest(l => limitedFunc(l))
  }
}
