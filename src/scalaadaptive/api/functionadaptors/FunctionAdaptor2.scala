package scalaadaptive.api.functionadaptors

import scalaadaptive.api.Implicits
import scalaadaptive.core.options.Measurement.Measurement
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{Measurement, RunOption, Storage}
import scalaadaptive.core.runtime.{Adaptive, MeasurementToken, ReferencedFunction}

/**
  * Created by pk250187 on 4/2/17.
  */
class FunctionAdaptor2[I1, I2, R](private val options: List[RunOption[(I1, I2) => R]],
                                  private val using: Measurement = Measurement.RunTime,
                                  private val bySelector: (I1, I2) => Int = null,
                                  private val storage: Storage = Storage.Global) extends ((I1, I2) => R) {
  private val customRunner = new CustomRunner(storage)

  def or[J1 <: I1, J2 <: I2, S >: R](fun: (J1, J2) => S): (J1, J2) => S = orAdaptor(Implicits.toAdaptor(fun))
  private def orAdaptor[J1 <: I1, J2 <: I2, S >: R](fun: FunctionAdaptor2[J1, J2, S]): (J1, J2) => S =
    new FunctionAdaptor2[J1, J2, S](this.options.map(opt => new RunOption[(J1, J2) => S]((a1, a2) => opt.function(a1, a2), opt.reference)) ++ fun.options)
  def by(selector: (I1, I2) => Int): (I1, I2) => R = new FunctionAdaptor2[I1, I2, R](options, using, selector)
  def using(measurement: Measurement): (I1, I2) => R = new FunctionAdaptor2[I1, I2, R](options, measurement, bySelector)

  private def generateOptions(v1: I1, v2: I2): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function(v1, v2) }, f.reference))

  private def createInputDescriptor(v1: I1, v2: I2) = if (bySelector != null) bySelector(v1, v2) else 0

  override def apply(v1: I1, v2: I2): R = {
    customRunner.runOption(generateOptions(v1, v2), createInputDescriptor(v1, v2))
  }

  def applyWithoutMeasuring(v1: I1, v2: I2): (R, MeasurementToken) = {
    customRunner.runOptionWithDelayedMeasure(generateOptions(v1, v2), createInputDescriptor(v1, v2))
  }
}