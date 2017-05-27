package scalaadaptive.core.adaptors

import scalaadaptive.api.Implicits
import scalaadaptive.api.adaptors.MultiFunction0
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{Selection, Storage}
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scalaadaptive.core.runtime.{FunctionRunner, AppliedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](private val options: List[RunOption[() => R]],
                          private val selector: Option[() => Int] = None,
                          private val storage: Storage = Storage.Global) extends MultiFunction0[R] with FunctionAdaptorCommon {
  override protected val runner = new CustomRunner(storage)
  override protected def functionReferences: Iterable[FunctionReference] = options.map(o => o.reference)

  override def or(fun: () => R): () => R =
    orAdaptor(Conversions.toAdaptor(fun))

  override def by(newSelector: () => Int): () => R =
    new FunctionAdaptor0[R](options, Some(newSelector), storage)
  override def using(newStorage: Storage): () => R =
    new FunctionAdaptor0[R](options, selector, newStorage)

  override def apply(): R = ??? /*{
    runner.runOption(generateOptions(), createInputDescriptor(), None, Selection.Continuous)
  }*/

  override def applyWithoutMeasuring(): (R, InvocationToken) = ??? /*{
    runner.runOptionWithDelayedMeasure(generateOptions(), createInputDescriptor(), None, Selection.Continuous)
  }*/

  private def orAdaptor(fun: FunctionAdaptor0[R]): () => R = new FunctionAdaptor0[R](this.options ++ fun.options)

  private def generateOptions(): Seq[AppliedFunction[R]] =
    options.map(f => new AppliedFunction[R]({ () => f.function() }, f.reference))

  private def createInputDescriptor(): Option[Long] =
    if (selector.isDefined) Some(selector.get())
    else None
}