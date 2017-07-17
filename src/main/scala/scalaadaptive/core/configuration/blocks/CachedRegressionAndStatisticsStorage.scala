package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedRegressionRunHistory, CachedStatisticsRunHistory, FullRunHistory}

/**
  * Created by Petr Kubat on 7/1/17.
  */
trait CachedRegressionAndStatisticsStorage extends BaseLongConfiguration {
  override def createHistoryStorage: HistoryStorage[TMeasurement] = {
    new MapHistoryStorage[TMeasurement](key =>
      new CachedStatisticsRunHistory[Long](new CachedRegressionRunHistory[Long](new FullRunHistory[Long](key)(num))(num))(num)
    )
  }
}
