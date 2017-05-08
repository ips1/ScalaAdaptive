package scalaadaptive.core.adaptors

import java.time.Duration

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.Storage
import scalaadaptive.core.runtime.{MeasurementToken, ReferencedFunction, TrainingHelper}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[T, R](private val options: List[RunOption[(T) => R]],
                             private val selector: Option[(T) => Int],
                             private val adaptorConfig: AdaptorConfig) extends MultiFunction1[T, R] {
  private val customRunner = new CustomRunner(adaptorConfig.storage)
  private val trainingHelper = new TrainingHelper(customRunner)

  // Necessary for being able to omit the _ operator in chained calls

//  def or(fun: (T) => R): FunctionAdaptor1[T, R] = orAdaptor(Implicits.toAdaptor(fun))

  override def by(newSelector: (T) => Int): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, Some(newSelector), adaptorConfig)
  override def using(newStorage: Storage): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.using(newStorage))
  override def limitedTo(newDuration: Duration): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.limitedTo(newDuration))
  override def asClosures(closureIdentification: Boolean): MultiFunction1[T, R] =
    new FunctionAdaptor1[T, R](options, selector, adaptorConfig.asClosures(closureIdentification))

  override def apply(v1: T): R = customRunner.runOption(generateOptions(v1), createInputDescriptor(v1), adaptorConfig.duration)

  override def applyWithoutMeasuring(v1: T): (R, MeasurementToken) =
    customRunner.runOptionWithDelayedMeasure(generateOptions(v1), createInputDescriptor(v1), adaptorConfig.duration)

  override def toDebugString: String =
    options.map(o => o.reference.toString).mkString(", ")

  private def generateOptions(v1: T): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function(v1) },
      if (adaptorConfig.closureReferences) f.closureReference else f.reference))

  private def createInputDescriptor(v1: T): Option[Long] =
    if (selector.isDefined) Some(selector.get(v1)) else None

  override def orMultiFunction(fun: FunctionAdaptor1[T, R]): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](this.options ++ fun.options, selector, adaptorConfig)

  override def train(data: Seq[T]): Unit = {
    data.foreach { d =>
      trainingHelper.train(generateOptions(d), createInputDescriptor(d))
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
