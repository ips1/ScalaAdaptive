package tutorials.policy

import scalaadaptive.api.Adaptive
import scalaadaptive.api.policies.{PauseSelectionAfterStreakPolicy, StopSelectingWhenDecidedPolicy}
import scalaadaptive.core.configuration.blocks.analytics.NoAnalyticsCollection
import scalaadaptive.core.configuration.blocks.logging.ConsoleLogging
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 7/18/17.
  */
object Policy {
  import scalaadaptive.api.Implicits._
  val fastHello = () => println("Hello World!")
  val slowHello = () => {
    Thread.sleep(10)
    println("Sloooooow Hello World!")
  }

  // Turning on the logging to see the difference
  Adaptive.initialize(new DefaultConfiguration with ConsoleLogging)

  // We can use Policy to limit the overhead - after selecting the same function 10 times in a row, it will keep
  // using it for the next 200 runs. In the logs, we can see that the selection is not happening.
  val hello = fastHello or slowHello withPolicy new PauseSelectionAfterStreakPolicy(10, 200)

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 100) { hello() }
  }
}
