package scalaadaptive.core.adaptors

import scalaadaptive.api.Implicits
import scalaadaptive.api.adaptors.MultiFunction2
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{RunOption, Storage}
import scalaadaptive.core.runtime.{MeasurementToken, ReferencedFunction}

/**
  * Created by pk250187 on 4/2/17.
  */
class FunctionAdaptor2[I1, I2, R](private val options: List[RunOption[(I1, I2) => R]],
                                  private val selector: Option[(I1, I2) => Int] = None,
                                  private val storage: Storage = Storage.Global) extends MultiFunction2[I1, I2, R] {
  private val customRunner = new CustomRunner(storage)

  override def or(fun: (I1, I2) => R): (I1, I2) => R =
    orAdaptor(Conversions.toAdaptor(fun))
  override def by(newSelector: (I1, I2) => Int): (I1, I2) => R =
    new FunctionAdaptor2[I1, I2, R](options, Some(newSelector), storage)
  override def using(newStorage: Storage) =
    new FunctionAdaptor2[I1, I2, R](options, selector, newStorage)


  override def apply(v1: I1, v2: I2): R = {
    customRunner.runOption(generateOptions(v1, v2), createInputDescriptor(v1, v2), None)
  }

  override def applyWithoutMeasuring(v1: I1, v2: I2): (R, MeasurementToken) = {
    customRunner.runOptionWithDelayedMeasure(generateOptions(v1, v2), createInputDescriptor(v1, v2), None)
  }

  override def toDebugString: String =
    options.map(o => o.reference.toString).mkString(", ")

  private def orAdaptor(fun: FunctionAdaptor2[I1, I2, R]): (I1, I2) => R =
    new FunctionAdaptor2[I1, I2, R](
      this.options.map(opt => new RunOption[(I1, I2) => R](
        (a1, a2) => opt.function(a1, a2), opt.reference)) ++ fun.options)

  private def generateOptions(v1: I1, v2: I2): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function(v1, v2) }, f.reference))

  private def createInputDescriptor(v1: I1, v2: I2): Option[Long] =
    if (selector.isDefined) Some(selector.get(v1, v2))
    else None
}