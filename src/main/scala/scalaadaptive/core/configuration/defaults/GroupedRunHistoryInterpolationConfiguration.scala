package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedGroupedRunHistory, ImmutableFullRunHistory}

/**
  * Created by pk250187 on 5/1/17.
  */
class GroupedRunHistoryInterpolationConfiguration
  extends BaseLongConfiguration
  with LoessInterpolationPredictiveStrategy
  with RunTimeMeasurement
  with DefaultHistoryPath
  with BufferedSerialization
  with TTestNonPredictiveStrategy
  with CachedGroupStorage
