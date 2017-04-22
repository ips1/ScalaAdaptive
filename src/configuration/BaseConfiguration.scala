package configuration

import grouping.{GroupSelector, LogarithmGroupSelector}
import logging.{ConsoleLogger, Logger}
import performance.{PerformanceProvider, RunTimeProvider}
import runtime.history.{FullRunHistory, HistoryStorage, MapHistoryStorage, RunData}
import runtime.selection.{RunSelector, SimpleRunSelector}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseConfiguration extends Configuration {
  override val historyStorageFactory: () => HistoryStorage[MeasurementType] = () => {
    new MapHistoryStorage[MeasurementType](ref =>
      new FullRunHistory[MeasurementType](ref, new ArrayBuffer[RunData[MeasurementType]]())
    )
  }
  override val groupSelector: GroupSelector = new LogarithmGroupSelector
  override val logger: Logger = new ConsoleLogger
}
