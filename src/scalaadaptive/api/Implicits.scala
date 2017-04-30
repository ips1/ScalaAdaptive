package scalaadaptive.api

import scala.reflect.macros.blackbox.Context
import scalaadaptive.api.functionadaptors.{FunctionAdaptor0, FunctionAdaptor1, FunctionAdaptor2}
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.options.RunOption
import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}

import scala.language.experimental.macros

/**
  * Created by pk250187 on 3/19/17.
  */
object Implicits {
  implicit def toAdaptor[R](fun: () => R): FunctionAdaptor0[R] = fun match {
    case adaptor: FunctionAdaptor0[R] => adaptor
    case _ => new FunctionAdaptor0[R](List(new RunOption(fun, ClosureNameReference(fun.getClass.getTypeName))))
  }

  implicit def macroToAdaptor[I, R](fun: (I) => R): FunctionAdaptor1[I, R] = macro macroToAdaptor_impl[I, R]

  def macroToAdaptor_impl[I, R](c: Context)(fun: c.Expr[(I) => R]): c.Expr[FunctionAdaptor1[I, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[FunctionAdaptor1[I, R]](resultTree)
  }

  def toAdaptor[I, R](fun: (I) => R): FunctionAdaptor1[I, R] = fun match {
      // TODO: Erasure not checking actual types?
    case adaptor: FunctionAdaptor1[I, R] => adaptor
    case _ => new FunctionAdaptor1[I, R](List(new RunOption(fun, ClosureNameReference(fun.getClass.getTypeName))))
  }

  def toAdaptor[I, R](fun: (I) => R, reference: FunctionReference): FunctionAdaptor1[I, R] = fun match {
    case adaptor: FunctionAdaptor1[I, R] => adaptor
    case _ => new FunctionAdaptor1[I, R](List(new RunOption(fun, reference)))
  }

  implicit def toAdaptor[I1, I2, R](fun: (I1, I2) => R): FunctionAdaptor2[I1, I2, R] = fun match {
    case adaptor: FunctionAdaptor2[I1, I2, R] => adaptor
    case _ => new FunctionAdaptor2[I1, I2, R](List(new RunOption(fun, ClosureNameReference(fun.getClass.getTypeName))))
  }
}
