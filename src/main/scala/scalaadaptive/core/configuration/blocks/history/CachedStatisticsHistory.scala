package scalaadaptive.core.configuration.blocks.history

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.{CachedStatisticsRunHistory, RunHistory}

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * Wraps the parent [[scalaadaptive.core.runtime.history.runhistory.RunHistory]] implementation into the
  * [[scalaadaptive.core.runtime.history.runhistory.CachedStatisticsRunHistory]].
  *
  * Note that it uses inherited (super) implementation of createRunHistory, therefore, the inheritance order matters.
  *
  */
trait CachedStatisticsHistory extends BaseLongConfiguration {
  override def createRunHistory(log: Logger, key: HistoryKey): RunHistory[Long] =
    new CachedStatisticsRunHistory[Long](super.createRunHistory(log, key))(num)
}
