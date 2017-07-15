package scalaadaptive.core.functions.adaptors

import scalaadaptive.core.functions.identifiers.{ClosureIdentifier, FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.functions.FunctionFactory

/**
  * Created by pk250187 on 5/27/17.
  */
object Conversions {
  private def functionFactory: FunctionFactory = AdaptiveInternal.getFunctionFactory

  private def generateClosureNameIdentifier(closure: Any) = ClosureIdentifier(closure.getClass.getTypeName)

  def toAdaptor[R](fun: () => R): FunctionAdaptor0[R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  def toAdaptor[R](fun: () => R, identifier: FunctionIdentifier): FunctionAdaptor0[R] = fun match {
    case adaptor: FunctionAdaptor0[R] => adaptor
    case _ =>
      new FunctionAdaptor0[R](
        functionFactory.createFunction[Unit, R](
          new IdentifiedFunction[Unit, R]((Unit) => fun(), generateClosureNameIdentifier(fun), identifier)))
  }

  def toAdaptor[T1, R](fun: (T1) => R): FunctionAdaptor1[T1, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  def toAdaptor[T1, R](fun: (T1) => R, identifier: FunctionIdentifier): FunctionAdaptor1[T1, R] = fun match {
    case adaptor: FunctionAdaptor1[T1, R] => adaptor
    case _ =>
      new FunctionAdaptor1[T1, R](
        functionFactory.createFunction[T1, R](
          new IdentifiedFunction[T1, R](fun, generateClosureNameIdentifier(fun), identifier)))
  }

  def toAdaptor[T1, T2, R](fun: (T1, T2) => R): FunctionAdaptor2[T1, T2, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  def toAdaptor[T1, T2, R](fun: (T1, T2) => R, identifier: FunctionIdentifier): FunctionAdaptor2[T1, T2, R] = fun match {
    case adaptor: FunctionAdaptor2[T1, T2, R] => adaptor
    case _ =>
      new FunctionAdaptor2[T1, T2, R](
        functionFactory.createFunction[(T1, T2), R](
          new IdentifiedFunction[(T1, T2), R]((t: (T1, T2)) => fun(t._1, t._2), generateClosureNameIdentifier(fun), identifier)))
  }

  def toAdaptor[T1, T2, T3, R](fun: (T1, T2, T3) => R): FunctionAdaptor3[T1, T2, T3, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  def toAdaptor[T1, T2, T3, R](fun: (T1, T2, T3) => R, identifier: FunctionIdentifier): FunctionAdaptor3[T1, T2, T3, R] = fun match {
    case adaptor: FunctionAdaptor3[T1, T2, T3, R] => adaptor
    case _ =>
      new FunctionAdaptor3[T1, T2, T3, R](
        functionFactory.createFunction[(T1, T2, T3), R](
          new IdentifiedFunction[(T1, T2, T3), R]((t: (T1, T2, T3)) => fun(t._1, t._2, t._3), generateClosureNameIdentifier(fun), identifier)))
  }

  def toAdaptor[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): FunctionAdaptor4[T1, T2, T3, T4, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  def toAdaptor[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R, identifier: FunctionIdentifier): FunctionAdaptor4[T1, T2, T3, T4, R] = fun match {
    case adaptor: FunctionAdaptor4[T1, T2, T3, T4, R] => adaptor
    case _ =>
      new FunctionAdaptor4[T1, T2, T3, T4, R](
        functionFactory.createFunction[(T1, T2, T3, T4), R](
          new IdentifiedFunction[(T1, T2, T3, T4), R]((t: (T1, T2, T3, T4)) => fun(t._1, t._2, t._3, t._4), generateClosureNameIdentifier(fun), identifier)))
  }

  def toAdaptor[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): FunctionAdaptor5[T1, T2, T3, T4, T5, R] = toAdaptor(fun, generateClosureNameIdentifier(fun))

  def toAdaptor[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R, identifier: FunctionIdentifier): FunctionAdaptor5[T1, T2, T3, T4, T5, R] = fun match {
    case adaptor: FunctionAdaptor5[T1, T2, T3, T4, T5, R] => adaptor
    case _ =>
      new FunctionAdaptor5[T1, T2, T3, T4, T5, R](
        functionFactory.createFunction[(T1, T2, T3, T4, T5), R](
          new IdentifiedFunction[(T1, T2, T3, T4, T5), R]((t: (T1, T2, T3, T4, T5)) => fun(t._1, t._2, t._3, t._4, t._5), generateClosureNameIdentifier(fun), identifier)))
  }
}
