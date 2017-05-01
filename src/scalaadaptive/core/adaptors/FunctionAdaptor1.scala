package scalaadaptive.core.adaptors

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context
import scalaadaptive.api.adaptors.MultiFunction1
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.options.{RunOption, Storage}
import scalaadaptive.core.runtime.{MeasurementToken, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[T, R](private val options: List[RunOption[(T) => R]],
                             private val selector: (T) => Int = null,
                             private val storage: Storage = Storage.Global) extends MultiFunction1[T, R] {
  private val customRunner = new CustomRunner(storage)

  // Necessary for being able to omit the _ operator in chained calls

//  def or(fun: (T) => R): FunctionAdaptor1[T, R] = orAdaptor(Implicits.toAdaptor(fun))

  override def by(newSelector: (T) => Int): (T) => R =
    new FunctionAdaptor1[T, R](options, newSelector, storage)
  override def using(newStorage: Storage): (T) => R =
    new FunctionAdaptor1[T, R](options, selector, newStorage)

  override def apply(v1: T): R = customRunner.runOption(generateOptions(v1), createInputDescriptor(v1))

  override def applyWithoutMeasuring(v1: T): (R, MeasurementToken) =
    customRunner.runOptionWithDelayedMeasure(generateOptions(v1), createInputDescriptor(v1))

  override def toDebugString: String =
    options.map(o => o.reference.toString).mkString(", ")

  private def generateOptions(v1: T): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function(v1) }, f.reference))

  private def createInputDescriptor(v1: T) =
    if (selector != null) selector(v1) else 0

  def orAdaptor(fun: FunctionAdaptor1[T, R]): FunctionAdaptor1[T, R] =
    new FunctionAdaptor1[T, R](
      this.options.map(opt => new RunOption[(T) => R](arg => opt.function(arg), opt.reference)) ++ fun.options)
}

object FunctionAdaptor1 {
  def or_impl[J, S](c: blackbox.Context)(fun: c.Expr[(J) => S]): c.Expr[FunctionAdaptor1[J, S]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[FunctionAdaptor1[J, S]](resultTree)
  }
}
