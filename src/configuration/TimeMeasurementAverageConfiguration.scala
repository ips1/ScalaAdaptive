package configuration

import performance.{PerformanceProvider, RunTimeProvider}
import runtime.selection.{BestAverageSelector, LowRunAwareSelector, RunSelector}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/26/17.
  */
object TimeMeasurementAverageConfiguration extends BaseLongConfiguration {
  override val runSelector: RunSelector[MeasurementType] = new LowRunAwareSelector[Long](new BestAverageSelector(), 20)
  override val performanceProvider: PerformanceProvider[MeasurementType] = new RunTimeProvider
}
