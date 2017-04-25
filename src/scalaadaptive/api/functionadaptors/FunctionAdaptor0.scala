package scalaadaptive.api.functionadaptors

import scalaadaptive.api.Implicits
import scalaadaptive.core.options.Measurement.Measurement
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{Measurement, RunOption, Storage}
import scalaadaptive.core.runtime.{Adaptive, MeasurementToken, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](private val options: List[RunOption[() => R]],
                          private val using: Measurement = Measurement.RunTime,
                          private val bySelector: () => Int = null,
                          private val storage: Storage = Storage.Global) extends (() => R) {
  private val customRunner = new CustomRunner(storage)
  def or(fun: () => R): () => R = orAdaptor(Implicits.toAdaptor(fun))
  private def orAdaptor(fun: FunctionAdaptor0[R]): () => R = new FunctionAdaptor0[R](this.options ++ fun.options)
  def by(selector: () => Int): () => R = new FunctionAdaptor0[R](options, using, selector)
  def using(measurement: Measurement): () => R = new FunctionAdaptor0[R](options, measurement, bySelector)

  private def generateOptions(): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function() }, f.reference))

  private def createInputDescriptor() = if (bySelector != null) bySelector() else 0

  override def apply(): R = {
    customRunner.runOption(generateOptions(), createInputDescriptor())
  }

  def applyWithoutMeasuring(): (R, MeasurementToken) = {
    customRunner.runOptionWithDelayedMeasure(generateOptions(), createInputDescriptor())
  }
}