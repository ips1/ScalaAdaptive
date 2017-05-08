package adaptivetests.delayed

import scalaadaptive.core.options.Storage

/**
  * Created by pk250187 on 4/23/17.
  */
object DelayedTest {
  import scalaadaptive.api.Implicits._
  def getFastConfig(): DelayedConfig = FastConfig()
  def getSlowConfig(): DelayedConfig = SlowConfig()
  val getConfig: () => DelayedConfig = getFastConfig _ or getSlowConfig using Storage.Persistent

  def run(config: DelayedConfig) = {
    config match {
      case SlowConfig() => Thread.sleep(50)
      case FastConfig() => Thread.sleep(10)
    }
  }

  def test() = {
    val (config, measure) = getConfig^()
    for (x <- 1 to 10) {
      measure(() => run(config))
    }
  }

  def main(args: Array[String]): Unit = {
    for (x <- 1 to 20) {
      test()
    }
  }
}
