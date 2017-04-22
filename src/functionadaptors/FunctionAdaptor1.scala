package functionadaptors

import macros.{AdaptiveMacros, AdaptiveMacrosHelper}
import options.Measurement.Measurement
import options.Storage.Storage
import options.{Measurement, RunOption, Storage}
import runtime.{Adaptive, ReferencedFunction}

import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.reflect.runtime.universe.{TypeTag, typeOf}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[T, R](private val options: List[RunOption[(T) => R]],
                             private val using: Measurement = Measurement.RunTime,
                             private val bySelector: (T) => Int = null,
                             protected val storage: Storage = Storage.Global) extends ((T) => R) with CustomRunner {
  // Necessary for being able to omit the _ operator in chained calls
  //def or[J <: T, S >: R](fun: (J) => S): (J) => S = macro FunctionAdaptor1.or_impl[J, S]

  def or[J <: T, S >: R](fun: (J) => S): (J) => S = orAdaptor(Implicits.toAdaptor(fun))
  def orAdaptor[J <: T, S >: R](fun: FunctionAdaptor1[J, S]): (J) => S =
    new FunctionAdaptor1[J, S](this.options.map(opt => new RunOption[(J) => S](arg => opt.function(arg), opt.reference)) ++ fun.options)
  def by(selector: (T) => Int): (T) => R = new FunctionAdaptor1[T, R](options, using, selector, storage)
  def withStorage(newStorage: Storage): (T) => R = new FunctionAdaptor1[T, R](options, using, bySelector, newStorage)
  def using(measurement: Measurement): (T) => R = new FunctionAdaptor1[T, R](options, measurement, bySelector, storage)

  override def apply(v1: T): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function(v1) }, f.reference))
    val by = if (bySelector != null) bySelector(v1) else 0
    runOption(test, by)
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
