import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.blocks.logging.{ConsoleLogging, FileLogging, NoLogging}
import scalaadaptive.core.configuration.blocks.persistence.BufferedPersistence
import scalaadaptive.core.configuration.blocks.selection.{TTestMeanBasedStrategy, UTestMeanBasedStrategy, WindowBoundRegressionInputBasedStrategy}
import scalaadaptive.core.configuration.blocks.history.{CachedRegressionHistory, CachedStatisticsHistory}
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 7/1/17.
  */
object ConfigurationTest {
  def main(args: Array[String]): Unit = {
    val config = new BaseLongConfiguration
      with RunTimeMeasurement
      with TTestMeanBasedStrategy
      with WindowBoundRegressionInputBasedStrategy
      with BufferedPersistence
      with NoLogging

    val config2 = new DefaultConfiguration
      with ConsoleLogging

    val config3 = new BaseLongConfiguration
      with BufferedPersistence
      with RunTimeMeasurement
      with WindowBoundRegressionInputBasedStrategy
      with UTestMeanBasedStrategy
      with CachedRegressionHistory
      with CachedStatisticsHistory
      with FileLogging {
      override val maximumNumberOfRecords = 20000
      override val alpha = 0.25
      override val logFilePath = "./adaptive/log.txt"
    }

    Adaptive.initialize(config)
  }
}
