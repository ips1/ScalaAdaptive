package scalaadaptive.core.adaptors

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime._
import scalaadaptive.core.runtime.invocationtokens.{InvocationToken, InvocationTokenWithCallbacks}

/**
  * Created by pk250187 on 4/22/17.
  */
class CustomRunner(val storage: Storage) extends FunctionRunner {
  private val runner: FunctionRunner = storage match {
    case Storage.Local => Adaptive.createRunner()
    case _ => null
  }

  private def selectRunner: FunctionRunner =
    storage match {
      case Storage.Local => runner
      case Storage.Persistent => Adaptive.persistentRunner
      case _ => Adaptive.runner
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
