package configuration

import grouping.{GroupSelector, LogarithmGroupSelector}
import performance.{PerformanceProvider, RunTimeProvider}
import runtime.history.{FullRunHistory, HistoryStorage, MapHistoryStorage, RunData}
import runtime.selection.{RunSelector, SimpleRunSelector}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/26/17.
  */
object TimeMeasurementDefaultConfiguration extends Configuration {
  type MeasurementType = Long
  override val historyStorage: HistoryStorage[MeasurementType] =
    new MapHistoryStorage[MeasurementType](ref => new FullRunHistory[MeasurementType](ref, new ArrayBuffer[RunData[MeasurementType]]()))
  override val runSelector: RunSelector[MeasurementType] = new SimpleRunSelector(20)
  override val performanceProvider: PerformanceProvider[MeasurementType] = new RunTimeProvider
  override val groupSelector: GroupSelector = new LogarithmGroupSelector
}
