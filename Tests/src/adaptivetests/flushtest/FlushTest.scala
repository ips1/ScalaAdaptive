package adaptivetests.flushtest

import adaptivetests.TestRunner
import adaptivetests.testmethods.TestMethods

import scalaadaptive.core.configuration.defaults.GroupedRunHistoryInterpolationConfiguration
import scalaadaptive.api.options.Storage
import scalaadaptive.api.Adaptive

/**
  * Created by pk250187 on 5/14/17.
  */
object FlushTest {
  def main(args: Array[String]): Unit = {
    import scalaadaptive.api.Implicits._

    val runner = new TestRunner()
    val testMethods = new TestMethods()
    Adaptive.initialize(GroupedRunHistoryInterpolationConfiguration)

    runner.runIncrementalTest(l => testMethods.function(l))

    // Flush just one of the methods:
    (testMethods.linearHighConstant _ storeUsing Storage.Persistent).flushHistory()

    runner.runIncrementalTest(l => testMethods.function(l))

    // Flush all of the methods:
    testMethods.function.flushHistory()

    runner.runIncrementalTest(l => testMethods.function(l))

    testMethods.function.flushHistory()
  }
}
