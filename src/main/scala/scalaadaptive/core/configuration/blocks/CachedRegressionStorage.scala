package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedRegressionRunHistory, CachedStatisticsRunHistory, FullRunHistory}

/**
  * Created by pk250187 on 7/1/17.
  */
trait CachedRegressionStorage extends BaseLongConfiguration  {
  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key =>
      new CachedRegressionRunHistory[Long](new FullRunHistory[Long](key)(num))(num)
    )
  }
}