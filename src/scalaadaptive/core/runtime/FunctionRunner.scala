package scalaadaptive.core.runtime

import java.time.Duration

import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by pk250187 on 3/26/17.
  */
trait FunctionRunner {
  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                             inputDescriptor: Option[Long],
                             limitedBy: Option[Duration]): TReturnType
  def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                               inputDescriptor: Option[Long],
                                               limitedBy: Option[Duration]): (TReturnType, MeasurementToken)
  def runMeasuredFunction[TReturnType](fun: () => TReturnType,
                                       key: HistoryKey,
                                       inputDescriptor: Option[Long],
                                       tracker: PerformanceTracker): TReturnType
}
