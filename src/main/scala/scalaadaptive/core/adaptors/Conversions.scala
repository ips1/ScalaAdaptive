package scalaadaptive.core.adaptors

import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}
import scalaadaptive.core.runtime.{AdaptiveInternal, FunctionFactory, ReferencedFunction}

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
          new ReferencedFunction[Unit, R]((Unit) => fun(), generateClosureNameReference(fun), reference)),
        functionFactory
      )
  }

  def toAdaptor[T1, R](fun: (T1) => R): FunctionAdaptor1[T1, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, R](fun: (T1) => R, reference: FunctionReference): FunctionAdaptor1[T1, R] = fun match {
    case adaptor: FunctionAdaptor1[T1, R] => adaptor
    case _ =>
      new FunctionAdaptor1[T1, R](
        functionFactory.createFunction[T1, R](
          new ReferencedFunction[T1, R](fun, generateClosureNameReference(fun), reference)),
        functionFactory
      )
  }

  def toAdaptor[T1, T2, R](fun: (T1, T2) => R): FunctionAdaptor2[T1, T2, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[T1, T2, R](fun: (T1, T2) => R, reference: FunctionReference): FunctionAdaptor2[T1, T2, R] = fun match {
    case adaptor: FunctionAdaptor2[T1, T2, R] => adaptor
    case _ =>
      new FunctionAdaptor2[T1, T2, R](
        functionFactory.createFunction[(T1, T2), R](
          new ReferencedFunction[(T1, T2), R]((t: (T1, T2)) => fun(t._1, t._2), generateClosureNameReference(fun), reference)),
        functionFactory
      )
  }
}
