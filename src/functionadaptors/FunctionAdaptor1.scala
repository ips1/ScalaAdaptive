package functionadaptors

import macros.{AdaptiveMacros, AdaptiveMacrosHelper}
import options.Measurement.Measurement
import options.Storage.Storage
import options.{Measurement, RunOption, Storage}
import runtime.{Adaptive, MeasurementToken, ReferencedFunction}

import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.reflect.runtime.universe.{TypeTag, typeOf}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[T, R](private val options: List[RunOption[(T) => R]],
                             private val using: Measurement = Measurement.RunTime,
                             private val bySelector: (T) => Int = null,
                             private val storage: Storage = Storage.Global) extends ((T) => R) {
  private val customRunner = new CustomRunner(storage)

  // Necessary for being able to omit the _ operator in chained calls
  //def or[J <: T, S >: R](fun: (J) => S): (J) => S = macro FunctionAdaptor1.or_impl[J, S]

  def or[J <: T, S >: R](fun: (J) => S): (J) => S = orAdaptor(Implicits.toAdaptor(fun))
  def orAdaptor[J <: T, S >: R](fun: FunctionAdaptor1[J, S]): (J) => S =
    new FunctionAdaptor1[J, S](this.options.map(opt => new RunOption[(J) => S](arg => opt.function(arg), opt.reference)) ++ fun.options)
  def by(selector: (T) => Int): (T) => R = new FunctionAdaptor1[T, R](options, using, selector, storage)
  def withStorage(newStorage: Storage): (T) => R = new FunctionAdaptor1[T, R](options, using, bySelector, newStorage)
  def using(measurement: Measurement): (T) => R = new FunctionAdaptor1[T, R](options, measurement, bySelector, storage)

  private def generateOptions(v1: T): Seq[ReferencedFunction[R]] =
    options.map(f => new ReferencedFunction[R]({ () => f.function(v1) }, f.reference))

  private def createInputDescriptor(v1: T) = if (bySelector != null) bySelector(v1) else 0

  override def apply(v1: T): R = {
    customRunner.runOption(generateOptions(v1), createInputDescriptor(v1))
  }

  def applyWithoutMeasuring(v1: T): (R, MeasurementToken) = {
    customRunner.runOptionWithDelayedMeasure(generateOptions(v1), createInputDescriptor(v1))
  }
}

object FunctionAdaptor1 {
  def or_impl[J, S](c: Context)(fun: c.Expr[(J) => S]): c.Expr[(J) => S] = {
    import c.universe.{TypeTree, Ident, newTypeName}
    val types = List(TypeTree().setOriginal(Ident(newTypeName("J"))), TypeTree().setOriginal(Ident(newTypeName("S"))))
    //val types = List(TypeTree(), TypeTree())
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree, types)
    println(resultTree)
    c.Expr[(J) => S](resultTree)
  }
}
