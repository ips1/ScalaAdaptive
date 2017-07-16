package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedGroupedRunHistory, ImmutableFullRunHistory}

/**
  * Created by Petr Kubat on 5/1/17.
  */
class GroupedRunHistoryInterpolationConfiguration
  extends BaseLongConfiguration
  with LoessInterpolationInputBasedStrategy
  with RunTimeMeasurement
  with DefaultHistoryPath
  with BufferedSerialization
  with TTestMeanBasedStrategy
  with CachedGroupStorage
