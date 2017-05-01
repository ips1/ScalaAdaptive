package scalaadaptive.core.configuration.defaults

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.runtime.history.serialization.{BasicHistorySerializer, BufferedHistorySerializer}
import scalaadaptive.core.runtime.selection.{BestAverageSelector, LowRunAwareSelector, RoundRobinSelector, RunSelector}

/**
  * Created by pk250187 on 5/1/17.
  */
trait BufferedSerialization extends BaseLongConfiguration {
  override val historySerializer = new BufferedHistorySerializer[Long](super.historySerializer, 20)
}
