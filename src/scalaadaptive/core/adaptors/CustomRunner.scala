package scalaadaptive.core.adaptors

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.{Adaptive, FunctionRunner, MeasurementToken, ReferencedFunction}

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
  override def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                      inputDescriptor: Option[Long],
                                      limitedBy: Option[Duration],
                                      selection: Selection): TReturnType =
    selectRunner.runOption(options, inputDescriptor, limitedBy, selection)

  override def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                                        inputDescriptor: Option[Long],
                                                        limitedBy: Option[Duration],
                                                        selection: Selection): (TReturnType, MeasurementToken) =
    selectRunner.runOptionWithDelayedMeasure(options, inputDescriptor, limitedBy, selection)

  override def runMeasuredFunction[TReturnType](fun: () => TReturnType,
                                                key: HistoryKey,
                                                inputDescriptor: Option[Long],
                                                tracker: PerformanceTracker): TReturnType =
    selectRunner.runMeasuredFunction(fun, key, inputDescriptor, tracker)

  override def flushHistory(reference: FunctionReference): Unit =
    selectRunner.flushHistory(reference)
}
