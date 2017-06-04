package scalaadaptive.core.runtime

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.invocationtokens.{InvocationToken, InvocationTokenWithCallbacks}

/**
  * Created by pk250187 on 3/26/17.
  *
  * Used to run one of multiple options passed in based on a history of previous runs of the options.
  *
  */
trait OptionRunner {
  def runOption[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                       arguments: TArgType,
                                       inputDescriptor: Option[Long],
                                       limitedBy: Option[Duration],
                                       selection: Selection): RunResult[TReturnType]

  def runOptionWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                         arguments: TArgType,
                                                         inputDescriptor: Option[Long],
                                                         limitedBy: Option[Duration],
                                                         selection: Selection): (TReturnType, InvocationTokenWithCallbacks)

  def flushHistory(reference: FunctionReference)
}
