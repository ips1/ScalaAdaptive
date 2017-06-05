package scalaadaptive.core.functions.adaptors

import scalaadaptive.core.functions.references.{ClosureNameReference, FunctionReference}
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.functions.{FunctionFactory, ReferencedFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
object Conversions {
  private def functionFactory: FunctionFactory = AdaptiveInternal.getFunctionFactory

  private def generateClosureNameReference(closure: Any) = ClosureNameReference(closure.getClass.getTypeName)

  def toAdaptor[R](fun: () => R): FunctionAdaptor0[R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[R](fun: () => R, reference: FunctionReference): FunctionAdaptor0[R] = fun match {
    case adaptor: FunctionAdaptor0[R] => adaptor
    case _ =>
      new FunctionAdaptor0[R](
        functionFactory.createFunction[Unit, R](
          new ReferencedFunction[Unit, R]((Unit) => fun(), generateClosureNameReference(fun), reference)))
  }

  def toAdaptor[T1, R](fun: (T1) => R): FunctionAdaptor1[T1, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, R](fun: (T1) => R, reference: FunctionReference): FunctionAdaptor1[T1, R] = fun match {
    case adaptor: FunctionAdaptor1[T1, R] => adaptor
    case _ =>
      new FunctionAdaptor1[T1, R](
        functionFactory.createFunction[T1, R](
          new ReferencedFunction[T1, R](fun, generateClosureNameReference(fun), reference)))
  }

  def toAdaptor[T1, T2, R](fun: (T1, T2) => R): FunctionAdaptor2[T1, T2, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, T2, R](fun: (T1, T2) => R, reference: FunctionReference): FunctionAdaptor2[T1, T2, R] = fun match {
    case adaptor: FunctionAdaptor2[T1, T2, R] => adaptor
    case _ =>
      new FunctionAdaptor2[T1, T2, R](
        functionFactory.createFunction[(T1, T2), R](
          new ReferencedFunction[(T1, T2), R]((t: (T1, T2)) => fun(t._1, t._2), generateClosureNameReference(fun), reference)))
  }

  def toAdaptor[T1, T2, T3, R](fun: (T1, T2, T3) => R): FunctionAdaptor3[T1, T2, T3, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, T2, T3, R](fun: (T1, T2, T3) => R, reference: FunctionReference): FunctionAdaptor3[T1, T2, T3, R] = fun match {
    case adaptor: FunctionAdaptor3[T1, T2, T3, R] => adaptor
    case _ =>
      new FunctionAdaptor3[T1, T2, T3, R](
        functionFactory.createFunction[(T1, T2, T3), R](
          new ReferencedFunction[(T1, T2, T3), R]((t: (T1, T2, T3)) => fun(t._1, t._2, t._3), generateClosureNameReference(fun), reference)))
  }

  def toAdaptor[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): FunctionAdaptor4[T1, T2, T3, T4, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R, reference: FunctionReference): FunctionAdaptor4[T1, T2, T3, T4, R] = fun match {
    case adaptor: FunctionAdaptor4[T1, T2, T3, T4, R] => adaptor
    case _ =>
      new FunctionAdaptor4[T1, T2, T3, T4, R](
        functionFactory.createFunction[(T1, T2, T3, T4), R](
          new ReferencedFunction[(T1, T2, T3, T4), R]((t: (T1, T2, T3, T4)) => fun(t._1, t._2, t._3, t._4), generateClosureNameReference(fun), reference)))
  }

  def toAdaptor[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): FunctionAdaptor5[T1, T2, T3, T4, T5, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R, reference: FunctionReference): FunctionAdaptor5[T1, T2, T3, T4, T5, R] = fun match {
    case adaptor: FunctionAdaptor5[T1, T2, T3, T4, T5, R] => adaptor
    case _ =>
      new FunctionAdaptor5[T1, T2, T3, T4, T5, R](
        functionFactory.createFunction[(T1, T2, T3, T4, T5), R](
          new ReferencedFunction[(T1, T2, T3, T4, T5), R]((t: (T1, T2, T3, T4, T5)) => fun(t._1, t._2, t._3, t._4, t._5), generateClosureNameReference(fun), reference)))
  }
}
