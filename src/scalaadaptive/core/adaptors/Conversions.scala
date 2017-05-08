package scalaadaptive.core.adaptors

import scalaadaptive.api.adaptors.{MultiFunction0, MultiFunction2}
import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}

/**
  * Created by pk250187 on 5/1/17.
  */
object Conversions {
  private def generateClosureNameReference(closure: Any) = ClosureNameReference(closure.getClass.getTypeName)

  def toAdaptor[R](fun: () => R): FunctionAdaptor0[R] = fun match {
    case adaptor: FunctionAdaptor0[R] => adaptor
    case _ => new FunctionAdaptor0[R](List(new RunOption(fun, generateClosureNameReference(fun))))
  }

  def toAdaptor[I, R](fun: (I) => R): FunctionAdaptor1[I, R] = fun match {
    // TODO: Erasure not checking actual types?
    case adaptor: FunctionAdaptor1[I, R] => adaptor
    case _ => new FunctionAdaptor1[I, R](List(new RunOption(fun, generateClosureNameReference(fun))))
  }

  def toAdaptor[I, R](fun: (I) => R, reference: FunctionReference): FunctionAdaptor1[I, R] = fun match {
    case adaptor: FunctionAdaptor1[I, R] => adaptor
    case _ => new FunctionAdaptor1[I, R](List(new RunOption(fun, generateClosureNameReference(fun), reference)))
  }

  def toAdaptor[I1, I2, R](fun: (I1, I2) => R): FunctionAdaptor2[I1, I2, R] = fun match {
    case adaptor: FunctionAdaptor2[I1, I2, R] => adaptor
    case _ => new FunctionAdaptor2[I1, I2, R](List(new RunOption(fun, generateClosureNameReference(fun))))
  }
}
