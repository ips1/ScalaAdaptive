package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.ImmutableFullRunHistory

/**
  * Created by Petr Kubat on 5/1/17.
  */
class ImmutableFullHistoryInterpolationConfiguration
  extends BaseLongConfiguration
    with LoessInterpolationInputBasedStrategy
    with RunTimeMeasurement
    with DefaultHistoryPath
    with BufferedSerialization
    with TTestMeanBasedStrategy {
  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key =>
      new ImmutableFullRunHistory[TMeasurement](key)(num)
    )
  }
}