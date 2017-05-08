package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.configuration.blocks._
import scalaadaptive.core.configuration.defaults.GroupedRunHistoryInterpolationConfiguration.{MeasurementType, num}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedStatisticsRunHistory, FullRunHistory, CachedGroupedRunHistory}

/**
  * Created by pk250187 on 5/2/17.
  */
object FullHistoryTTestConfiguration
  extends BaseLongConfiguration
    with TTestSelection
    with RunTimeMeasurement
    with DefaultPath
    with BufferedSerialization {
  override val historyStorageFactory: () => HistoryStorage[MeasurementType] = () => {
    new MapHistoryStorage[MeasurementType](key =>
      new CachedStatisticsRunHistory[Long](new FullRunHistory[Long](key)(num))(num)
    )
  }
}