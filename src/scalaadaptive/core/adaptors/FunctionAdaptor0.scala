package scalaadaptive.core.adaptors

import scalaadaptive.api.Implicits
import scalaadaptive.api.adaptors.MultiFunction0
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{RunOption, Storage}
import scalaadaptive.core.runtime.{MeasurementToken, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](private val options: List[RunOption[() => R]],
                          private val selector: Option[() => Int] = None,
                          private val storage: Storage = Storage.Global) extends MultiFunction0[R] {
  private val customRunner = new CustomRunner(storage)

  override def or(fun: () => R): () => R =
    orAdaptor(Conversions.toAdaptor(fun))

  override def by(newSelector: () => Int): () => R =
    new FunctionAdaptor0[R](options, Some(newSelector), storage)
  override def using(newStorage: Storage): () => R =
    new FunctionAdaptor0[R](options, selector, newStorage)

  override def apply(): R = {
    customRunner.runOption(generateOptions(), createInputDescriptor(), None)
  }

  override def applyWithoutMeasuring(): (R, MeasurementToken) = {
    customRunner.runOptionWithDelayedMeasure(generateOptions(), createInputDescriptor(), None)
  }

  override def toDebugString: String =
    options.map(o => o.reference.toString).mkString(", ")

  private def orAdaptor(fun: FunctionAdaptor0[R]): () => R = new FunctionAdaptor0[R](this.options ++ fun.options)

  private def generateOptions(): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function() }, f.reference))

  private def createInputDescriptor(): Option[Long] =
    if (selector.isDefined) Some(selector.get())
    else None
}