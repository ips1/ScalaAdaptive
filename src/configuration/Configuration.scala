package configuration
import grouping.{GroupSelector, LogarithmGroupSelector}
import performance.{PerformanceProvider, RunTimeProvider}
import runtime.history.{FullRunHistory, HistoryStorage, MapHistoryStorage, RunData}
import runtime.selection.{RunSelector, SimpleRunSelector}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/26/17.
  */
trait Configuration {
  type MeasurementType
  val historyStorage: HistoryStorage[MeasurementType]
  val runSelector: RunSelector[MeasurementType]
  val performanceProvider: PerformanceProvider[MeasurementType]
  val groupSelector: GroupSelector
}
