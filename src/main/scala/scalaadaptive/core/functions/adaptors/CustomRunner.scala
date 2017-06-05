package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.functions.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime._
import scalaadaptive.core.runtime.invocationtokens.InvocationTokenWithCallbacks
import scalaadaptive.core.functions.{ReferencedFunction, RunResult}

/**
  * Created by pk250187 on 4/22/17.
  */
class CustomRunner(val storage: Storage) extends AdaptiveRunner {
  private val runner: AdaptiveRunner = storage match {
    case Storage.Local => AdaptiveInternal.createRunner()
    case _ => null
  }

  private def selectRunner: AdaptiveRunner =
    storage match {
      case Storage.Local => runner
      case Storage.Persistent => AdaptiveInternal.persistentRunner
      case _ => AdaptiveInternal.runner
    }

  // Delegations
  override def runOption[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                arguments: TArgType,
                                                inputDescriptor: Option[Long],
                                                limitedBy: Option[Duration],
                                                selection: Selection): RunResult[TReturnType] =
    selectRunner.runOption(options, arguments, inputDescriptor, limitedBy, selection)

  override def runOptionWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                                  arguments: TArgType,
                                                                  inputDescriptor: Option[Long],
                                                                  limitedBy: Option[Duration],
                                                                  selection: Selection): (TReturnType, InvocationTokenWithCallbacks) =
    selectRunner.runOptionWithDelayedMeasure(options, arguments, inputDescriptor, limitedBy, selection)

  override def flushHistory(reference: FunctionReference): Unit =
    selectRunner.flushHistory(reference)
}
