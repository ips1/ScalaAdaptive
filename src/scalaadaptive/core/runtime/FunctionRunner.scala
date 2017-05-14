package scalaadaptive.core.runtime

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by pk250187 on 3/26/17.
  */
trait FunctionRunner {
  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                             inputDescriptor: Option[Long],
                             limitedBy: Option[Duration],
                             selection: Selection): TReturnType

  def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                               inputDescriptor: Option[Long],
                                               limitedBy: Option[Duration],
                                               selection: Selection): (TReturnType, MeasurementToken)

  def runMeasuredFunction[TReturnType](fun: () => TReturnType,
                                       key: HistoryKey,
                                       inputDescriptor: Option[Long],
                                       tracker: PerformanceTracker): TReturnType

  def flushHistory(reference: FunctionReference)
}
