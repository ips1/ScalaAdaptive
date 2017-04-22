package configuration
import grouping.GroupSelector
import logging.Logger
import performance.PerformanceProvider
import runtime.history.HistoryStorage
import runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/26/17.
  */
trait Configuration {
  type MeasurementType
  val historyStorageFactory: () => HistoryStorage[MeasurementType]
  val runSelector: RunSelector[MeasurementType]
  val performanceProvider: PerformanceProvider[MeasurementType]
  val groupSelector: GroupSelector
  val logger: Logger
}
