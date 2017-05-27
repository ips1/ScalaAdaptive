package scalaadaptive.core.adaptors

import scalaadaptive.api.Implicits
import scalaadaptive.api.adaptors.MultiFunction2
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{Selection, Storage}
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.AppliedFunction
import scalaadaptive.core.runtime.invocationtokens.InvocationToken

/**
  * Created by pk250187 on 4/2/17.
  */
class FunctionAdaptor2[I1, I2, R](private val options: List[RunOption[(I1, I2) => R]],
                                  private val selector: Option[(I1, I2) => Int] = None,
                                  private val storage: Storage = Storage.Global) extends MultiFunction2[I1, I2, R] with FunctionAdaptorCommon {
  override protected val runner = new CustomRunner(storage)
  override protected def functionReferences: Iterable[FunctionReference] = options.map(o => o.reference)

  override def or(fun: (I1, I2) => R): (I1, I2) => R =
    orAdaptor(Conversions.toAdaptor(fun))
  override def by(newSelector: (I1, I2) => Int): (I1, I2) => R =
    new FunctionAdaptor2[I1, I2, R](options, Some(newSelector), storage)
  override def using(newStorage: Storage) =
    new FunctionAdaptor2[I1, I2, R](options, selector, newStorage)


  override def apply(v1: I1, v2: I2): R = ???
    /*runner
      .runOption(generateOptions(v1, v2), createInputDescriptor(v1, v2), None, Selection.Continuous)*/

  override def applyWithoutMeasuring(v1: I1, v2: I2): (R, InvocationToken) = ???
    /*runner
      .runOptionWithDelayedMeasure(generateOptions(v1, v2), createInputDescriptor(v1, v2), None, Selection.Continuous)*/

  private def orAdaptor(fun: FunctionAdaptor2[I1, I2, R]): (I1, I2) => R =
    new FunctionAdaptor2[I1, I2, R](this.options ++ fun.options)

  private def generateOptions(v1: I1, v2: I2): Seq[AppliedFunction[R]] =
    options.map(f => new AppliedFunction[R]({ () => f.function(v1, v2) }, f.reference))

  private def createInputDescriptor(v1: I1, v2: I2): Option[Long] =
    if (selector.isDefined) Some(selector.get(v1, v2))
    else None
}