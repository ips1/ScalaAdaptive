package scalaadaptive.core.configuration.blocks.storage

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.{CachedRegressionRunHistory, RunHistory}

/**
  * Created by Petr Kubat on 7/1/17.
  */
trait CachedRegressionStorage extends BaseLongConfiguration  {
  override def createRunHistory(key: HistoryKey): RunHistory[Long] =
    new CachedRegressionRunHistory[Long](super.createRunHistory(key))(num)
}
