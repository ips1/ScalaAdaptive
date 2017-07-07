package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedGroupedRunHistory, ImmutableFullRunHistory}

/**
  * Created by pk250187 on 7/1/17.
  */
trait CachedGroupStorage extends BaseLongConfiguration {
  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key =>
      new CachedGroupedRunHistory[Long](new ImmutableFullRunHistory[Long](key)(num))(num)
    )
  }
}
