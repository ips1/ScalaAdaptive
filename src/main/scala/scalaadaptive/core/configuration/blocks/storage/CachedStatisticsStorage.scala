package scalaadaptive.core.configuration.blocks.storage

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.{CachedStatisticsRunHistory, RunHistory}

/**
  * Created by Petr Kubat on 6/5/17.
  */
trait CachedStatisticsStorage extends BaseLongConfiguration {
  override def createRunHistory(key: HistoryKey): RunHistory[Long] =
    new CachedStatisticsRunHistory[Long](super.createRunHistory(key))(num)
}
