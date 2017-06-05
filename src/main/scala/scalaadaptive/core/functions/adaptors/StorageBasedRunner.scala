package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime._
import scalaadaptive.core.runtime.invocationtokens.InvocationTokenWithCallbacks
import scalaadaptive.core.functions.RunResult

/**
  * Created by pk250187 on 4/22/17.
  */
class StorageBasedRunner(val storage: Storage) extends AdaptiveRunner {
  private val runner: AdaptiveRunner = storage match {
    case Storage.Local => AdaptiveInternal.createNewRunner()
    case _ => null
  }

  private def selectRunner: AdaptiveRunner =
    storage match {
      case Storage.Local => runner
      case Storage.Persistent => AdaptiveInternal.getSharedPersistentRunner
      case _ => AdaptiveInternal.getSharedRunner
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
