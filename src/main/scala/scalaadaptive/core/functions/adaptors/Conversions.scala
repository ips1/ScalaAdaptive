package scalaadaptive.core.functions.adaptors

import scalaadaptive.core.functions.identifiers.{ClosureIdentifier, FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.functions.FunctionFactory

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * Singleton containing the actual conversion methods from functions to corresponding adaptor types
  * (see [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]]). This implementation should, however, never be
  * used directly, only through macros from the [[scalaadaptive.api.Implicits]] class and or methods of adaptive
  * functions (see [[scalaadaptive.api.functions.AdaptiveFunction0]]).
  *
  * The reason are two:
  * - it does not perform the method name extraction, it can only accept it as a special identifier, otherwise, it uses
  * the closure name as the special identifier as well (see
  * [[scalaadaptive.core.functions.identifiers.IdentifiedFunction]])
  * - it returns concrete [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] (and similar) types which should remain hidden from the end user
  *
  * The adaptor types serve as adaptors between the Scala function interface and the internal representation of
  * functions with multiple implementations - [[scalaadaptive.core.functions.CombinedFunction]].
  *
  * The internal process is simple - the [[scalaadaptive.core.functions.FunctionFactory]] implementation fetched
  * from the [[scalaadaptive.core.runtime.AdaptiveInternal]] is used to create a new
  * [[scalaadaptive.core.functions.CombinedFunction]] instance containing given function, which is then wrapped with
  * the adaptor that provides the required interface.
  *
  */
object Conversions {
  private def functionFactory: FunctionFactory = AdaptiveInternal.getFunctionFactory

  private def generateClosureNameIdentifier(closure: Any) = ClosureIdentifier(closure.getClass.getTypeName)

  /** Converts a function with no arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with no special identifier. */
  def toAdaptor[R](fun: () => R): FunctionAdaptor0[R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  /** Converts a function with no arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with the specified special identifier. */
  def toAdaptor[R](fun: () => R, identifier: FunctionIdentifier): FunctionAdaptor0[R] = fun match {
    case adaptor: FunctionAdaptor0[R] => adaptor
    case _ =>
      new FunctionAdaptor0[R](
        functionFactory.createFunction[Unit, R](
          new IdentifiedFunction[Unit, R]((_) => fun(), generateClosureNameIdentifier(fun), identifier)))
  }

  /** Converts a function with one argument to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with no special identifier. */
  def toAdaptor[T1, R](fun: (T1) => R): FunctionAdaptor1[T1, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  /** Converts a function with one argument to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with the specified special identifier. */
  def toAdaptor[T1, R](fun: (T1) => R, identifier: FunctionIdentifier): FunctionAdaptor1[T1, R] = fun match {
    case adaptor: FunctionAdaptor1[T1, R] => adaptor
    case _ =>
      new FunctionAdaptor1[T1, R](
        functionFactory.createFunction[T1, R](
          new IdentifiedFunction[T1, R](fun, generateClosureNameIdentifier(fun), identifier)))
  }

  /** Converts a function with two arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with no special identifier. */
  def toAdaptor[T1, T2, R](fun: (T1, T2) => R): FunctionAdaptor2[T1, T2, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  /** Converts a function with two arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with the specified special identifier. */
  def toAdaptor[T1, T2, R](fun: (T1, T2) => R, identifier: FunctionIdentifier): FunctionAdaptor2[T1, T2, R] = fun match {
    case adaptor: FunctionAdaptor2[T1, T2, R] => adaptor
    case _ =>
      new FunctionAdaptor2[T1, T2, R](
        functionFactory.createFunction[(T1, T2), R](
          new IdentifiedFunction[(T1, T2), R]((t: (T1, T2)) => fun(t._1, t._2), generateClosureNameIdentifier(fun), identifier)))
  }

  /** Converts a function with three arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with no special identifier. */
  def toAdaptor[T1, T2, T3, R](fun: (T1, T2, T3) => R): FunctionAdaptor3[T1, T2, T3, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  /** Converts a function with three arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with the specified special identifier. */
  def toAdaptor[T1, T2, T3, R](fun: (T1, T2, T3) => R, identifier: FunctionIdentifier): FunctionAdaptor3[T1, T2, T3, R] = fun match {
    case adaptor: FunctionAdaptor3[T1, T2, T3, R] => adaptor
    case _ =>
      new FunctionAdaptor3[T1, T2, T3, R](
        functionFactory.createFunction[(T1, T2, T3), R](
          new IdentifiedFunction[(T1, T2, T3), R]((t: (T1, T2, T3)) => fun(t._1, t._2, t._3), generateClosureNameIdentifier(fun), identifier)))
  }

  /** Converts a function with four arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with no special identifier. */
  def toAdaptor[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): FunctionAdaptor4[T1, T2, T3, T4, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  /** Converts a function with four arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with the specified special identifier. */
  def toAdaptor[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R, identifier: FunctionIdentifier): FunctionAdaptor4[T1, T2, T3, T4, R] = fun match {
    case adaptor: FunctionAdaptor4[T1, T2, T3, T4, R] => adaptor
    case _ =>
      new FunctionAdaptor4[T1, T2, T3, T4, R](
        functionFactory.createFunction[(T1, T2, T3, T4), R](
          new IdentifiedFunction[(T1, T2, T3, T4), R]((t: (T1, T2, T3, T4)) => fun(t._1, t._2, t._3, t._4), generateClosureNameIdentifier(fun), identifier)))
  }

  /** Converts a function with five arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with no special identifier. */
  def toAdaptor[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): FunctionAdaptor5[T1, T2, T3, T4, T5, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  /** Converts a function with five arguments to [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] containing one
    * implementation with the specified special identifier. */
  def toAdaptor[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R, identifier: FunctionIdentifier): FunctionAdaptor5[T1, T2, T3, T4, T5, R] = fun match {
    case adaptor: FunctionAdaptor5[T1, T2, T3, T4, T5, R] => adaptor
    case _ =>
      new FunctionAdaptor5[T1, T2, T3, T4, T5, R](
        functionFactory.createFunction[(T1, T2, T3, T4, T5), R](
          new IdentifiedFunction[(T1, T2, T3, T4, T5), R]((t: (T1, T2, T3, T4, T5)) => fun(t._1, t._2, t._3, t._4, t._5), generateClosureNameIdentifier(fun), identifier)))
  }
}
