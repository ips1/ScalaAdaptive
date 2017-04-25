package scalaadaptive.core.configuration

import scalaadaptive.core.grouping.{GroupSelector, LogarithmGroupSelector}
import scalaadaptive.core.logging.{ConsoleLogger, Logger}
import scalaadaptive.core.runtime.history.{FullRunHistory, HistoryStorage, MapHistoryStorage, RunData}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseConfiguration extends Configuration {
  protected val num: Numeric[MeasurementType]
  override val historyStorageFactory: () => HistoryStorage[MeasurementType] = () => {
    new MapHistoryStorage[MeasurementType](key =>
      new FullRunHistory[MeasurementType](key, new ArrayBuffer[RunData[MeasurementType]]())(num)
    )
  }
  override val groupSelector: GroupSelector = new LogarithmGroupSelector
  override val logger: Logger = new ConsoleLogger
}
