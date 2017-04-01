package runtime

import configuration.{Configuration, TimeMeasurementDefaultConfiguration}
import grouping.LogarithmGroupSelector
import performance.RunTimeProvider
import runtime.history.{FullRunHistory, MapHistoryStorage, RunData, RunHistory}
import runtime.selection.SimpleRunSelector

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/19/17.
  */
object Adaptive {
  def initTracker(configuration: Configuration): FunctionRunner = {
    new RunTracker[configuration.MeasurementType](
      configuration.historyStorage,
      configuration.runSelector,
      configuration.performanceProvider,
      configuration.groupSelector,
      configuration.logger)
  }

  lazy val tracker = initTracker(TimeMeasurementDefaultConfiguration)
}
