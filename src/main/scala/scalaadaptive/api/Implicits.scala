package scalaadaptive.api

import scala.reflect.macros.blackbox.Context
import scalaadaptive.core.macros.AdaptiveMacrosHelper
import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors.{MultiFunction0, MultiFunction1, MultiFunction2}
import scalaadaptive.core.adaptors._

/**
  * Created by pk250187 on 3/19/17.
  */
object Implicits {
  implicit def macroToAdaptor0[R](fun: () => R): MultiFunction0[R] = macro macroToAdaptor0_impl[R]

  def macroToAdaptor0_impl[R](c: blackbox.Context)(fun: c.Expr[() => R]): c.Expr[MultiFunction0[R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction0[R]](resultTree)
  }

  implicit def macroToAdaptor1[T1, R](fun: (T1) => R): MultiFunction1[T1, R] = macro macroToAdaptor1_impl[T1, R]

  def macroToAdaptor1_impl[T1, R](c: blackbox.Context)(fun: c.Expr[(T1) => R]): c.Expr[MultiFunction1[T1, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction1[T1, R]](resultTree)
  }

  implicit def macroToAdaptor2[T1, T2, R](fun: (T1, T2) => R): MultiFunction2[T1, T2, R] = macro macroToAdaptor2_impl[T1, T2, R]

  def macroToAdaptor2_impl[T1, T2, R](c: blackbox.Context)(fun: c.Expr[(T1, T2) => R]): c.Expr[MultiFunction2[T1, T2, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction2[T1, T2, R]](resultTree)
  }
}
