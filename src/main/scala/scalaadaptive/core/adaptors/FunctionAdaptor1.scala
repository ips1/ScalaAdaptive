package scalaadaptive.core.adaptors

import java.time.Duration

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.invocationtokens.InvocationToken
import scalaadaptive.core.runtime.{AppliedFunction, ReferencedFunction, TrainingHelper}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[T, R](private val options: List[RunOption[(T) => R]],
                             private val selector: Option[(T) => Int],
                             private val adaptorConfig: AdaptorConfig) extends MultiFunction1[T, R] with FunctionAdaptorCommon {
  override protected val runner = new CustomRunner(adaptorConfig.storage)
  private val trainingHelper = new TrainingHelper(runner)
  private val referencedFunctions = List() /*options.map(f => new ReferencedFunction[T, R](f.function,
    if (adaptorConfig.closureReferences) f.closureReference else f.reference))*/
  override protected def functionReferences: Iterable[FunctionReference] = ??? /*referencedFunctions.map(o => o.reference)*/

  // Necessary for being able to omit the _ operator in chained calls

//  def or(fun: (T) => R): FunctionAdaptor1[T, R] = orAdaptor(Implicits.toAdaptor(fun))

  override def by(newSelector: (T) => Int): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, Some(newSelector), adaptorConfig)
  override def selectUsing(newSelection: Selection): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.selectUsing(newSelection))
  override def storeUsing(newStorage: Storage): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.storeUsing(newStorage))
  override def limitedTo(newDuration: Duration): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.limitedTo(newDuration))
  override def asClosures(closureIdentification: Boolean): MultiFunction1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.asClosures(closureIdentification))

  override def apply(v1: T): R = ???
    /*runner
      .runOption(generateOptions(v1), createInputDescriptor(v1), adaptorConfig.duration, adaptorConfig.selection)*/

  override def applyWithoutMeasuring(v1: T): (R, InvocationToken) = ???
    /*runner
      .runOptionWithDelayedMeasure(generateOptions(v1), createInputDescriptor(v1), adaptorConfig.duration, adaptorConfig.selection)*/

  private def generateOptions(v1: T): Seq[AppliedFunction[R]] =
    options.map(f => new AppliedFunction[R]({ () => f.function(v1) },
      if (adaptorConfig.closureReferences) f.closureReference else f.reference))

  private def createInputDescriptor(v1: T): Option[Long] =
    if (selector.isDefined) Some(selector.get(v1)) else None

  override def orMultiFunction(fun: FunctionAdaptor1[T, R]): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](this.options ++ fun.options, selector, adaptorConfig)

  override def train(data: Seq[T]): Unit = {
    data.foreach { d =>
      trainingHelper.train(generateOptions(d), createInputDescriptor(d), adaptorConfig.selection)
    }
  }
}

object FunctionAdaptor1 {
  def or_impl[J, S](c: blackbox.Context)(fun: c.Expr[(J) => S]): c.Expr[FunctionAdaptor1[J, S]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[FunctionAdaptor1[J, S]](resultTree)
  }
}
