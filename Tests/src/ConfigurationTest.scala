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
      with TTestMeanBasedStrategy
      with WindowBoundRegressionInputBasedStrategy
      with DefaultHistoryPath
      with BufferedSerialization
      with NoLogging

    val config2 = new DefaultConfiguration
      with ConsoleLogging

    val config3 = new BaseLongConfiguration
      with DefaultHistoryPath
      with RunTimeMeasurement
      with WindowBoundRegressionInputBasedStrategy
      with UTestMeanBasedStrategy
      with CachedRegressionAndStatisticsStorage
      with FileLogging {
      override val maximumNumberOfRecords = 20000
      override val alpha = 0.25
      override val logFilePath = "./adaptive/log.txt"
    }

    Adaptive.initialize(config)
  }
}
