package functionadaptors

import options.RunOption
import references.{ClosureNameReference, FunctionReference}

import scala.language.implicitConversions

/**
  * Created by pk250187 on 3/19/17.
  */
object Implicits {
  implicit def toAdaptor[I, R](fun: (I) => R): FunctionAdaptor1[I, R] = fun match {
    case adaptor: FunctionAdaptor1[I, R] => adaptor
    case _ => new FunctionAdaptor1[I, R](List(new RunOption(fun, ClosureNameReference(fun.getClass.getTypeName))))
  }

  implicit def toAdaptor[I, R](fun: (I) => R, reference: FunctionReference): FunctionAdaptor1[I, R] = fun match {
    case adaptor: FunctionAdaptor1[I, R] => adaptor
    case _ => new FunctionAdaptor1[I, R](List(new RunOption(fun, reference)))
  }

  implicit def toAdaptor[I1, I2, R](fun: (I1, I2) => R): FunctionAdaptor2[I1, I2, R] = fun match {
    case adaptor: FunctionAdaptor2[I1, I2, R] => adaptor
    case _ => new FunctionAdaptor2[I1, I2, R](List(new RunOption(fun, ClosureNameReference(fun.getClass.getTypeName))))
  }

  implicit def toAdaptor[R](fun: () => R): FunctionAdaptor0[R] = fun match {
    case adaptor: FunctionAdaptor0[R] => adaptor
    case _ => new FunctionAdaptor0[R](List(new RunOption(fun, ClosureNameReference(fun.getClass.getTypeName))))
  }
}
