package adaptivetests

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._

/**
  * Created by pk250187 on 7/10/17.
  */
abstract class TestConfiguration extends BaseLongConfiguration
  with RunTimeMeasurement
  with LinearRegressionInputBasedStrategy
  with TTestMeanBasedStrategy
  with DefaultHistoryPath
  with BufferedSerialization
  with NoLogging