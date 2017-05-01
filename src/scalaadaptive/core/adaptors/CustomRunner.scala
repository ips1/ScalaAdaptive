package scalaadaptive.core.adaptors

import scalaadaptive.core.options.Storage
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.performance.PerformanceTracker
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

  override def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                      inputDescriptor: Long = 0): TReturnType =
    selectRunner.runOption(options, inputDescriptor)

  override def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                                        inputDescriptor: Long = 0): (TReturnType, MeasurementToken) =
    selectRunner.runOptionWithDelayedMeasure(options, inputDescriptor)

  override def runMeasuredFunction[TReturnType](fun: () => TReturnType,
                                                key: HistoryKey,
                                                inputDescriptor: Long,
                                                tracker: PerformanceTracker): TReturnType =
    selectRunner.runMeasuredFunction(fun, key, inputDescriptor, tracker)
}
