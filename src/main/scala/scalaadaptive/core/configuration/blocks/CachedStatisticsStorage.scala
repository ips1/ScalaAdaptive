package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.{BaseLongConfiguration, Configuration}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedStatisticsRunHistory, FullRunHistory}

/**
  * Created by Petr Kubat on 6/5/17.
  */
trait CachedStatisticsStorage extends BaseLongConfiguration {
  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key =>
      new CachedStatisticsRunHistory[Long](new FullRunHistory[Long](key)(num))(num)
    )
  }
}
