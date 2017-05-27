package scalaadaptive.api

import scala.reflect.macros.blackbox.Context
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors.{MultiFunction0, MultiFunction2}
import scalaadaptive.api.newadaptors.MultiFunction1
import scalaadaptive.core.adaptors._

/**
  * Created by pk250187 on 3/19/17.
  */
object Implicits {
  implicit def toAdaptor[R](fun: () => R): MultiFunction0[R] = Conversions.toAdaptor(fun)

  implicit def macroToAdaptor[I, R](fun: (I) => R): MultiFunction1[I, R] = macro macroToAdaptor_impl[I, R]

  def macroToAdaptor_impl[I, R](c: blackbox.Context)(fun: c.Expr[(I) => R]): c.Expr[MultiFunction1[I, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction1[I, R]](resultTree)
  }

  implicit def toAdaptor[I1, I2, R](fun: (I1, I2) => R): MultiFunction2[I1, I2, R] = Conversions.toAdaptor(fun)
}
