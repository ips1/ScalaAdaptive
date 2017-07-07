import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by pk250187 on 7/1/17.
  */
object ConfigurationTest {
  def main(args: Array[String]): Unit = {
    val config = new BaseLongConfiguration
      with RunTimeMeasurement
      with TTestNonPredictiveStrategy
      with LimitedRegressionPredictiveStrategy
      with DefaultHistoryPath
      with BufferedSerialization
      with NoLogging

    val config2 = new DefaultConfiguration
      with ConsoleLogging

    Adaptive.initialize(config)
  }
}
