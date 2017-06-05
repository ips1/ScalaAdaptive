package scalaadaptive.api

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors._
import scalaadaptive.core.macros.AdaptiveMacrosHelper

/**
  * Created by pk250187 on 3/19/17.
  */
object Implicits {
  implicit def macroToAdaptor0[R](fun: () => R): MultiFunction0[R] =
    macro macroToAdaptor0_impl[R]

  def macroToAdaptor0_impl[R](c: blackbox.Context)(fun: c.Expr[() => R]): c.Expr[MultiFunction0[R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction0[R]](resultTree)
  }

  implicit def macroToAdaptor1[T1, R](fun: (T1) => R): MultiFunction1[T1, R] =
    macro macroToAdaptor1_impl[T1, R]

  def macroToAdaptor1_impl[T1, R](c: blackbox.Context)(fun: c.Expr[(T1) => R]): c.Expr[MultiFunction1[T1, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction1[T1, R]](resultTree)
  }

  implicit def macroToAdaptor2[T1, T2, R](fun: (T1, T2) => R): MultiFunction2[T1, T2, R] =
    macro macroToAdaptor2_impl[T1, T2, R]

  def macroToAdaptor2_impl[T1, T2, R](c: blackbox.Context)(fun: c.Expr[(T1, T2) => R]): c.Expr[MultiFunction2[T1, T2, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction2[T1, T2, R]](resultTree)
  }

  implicit def macroToAdaptor3[T1, T2, T3, R](fun: (T1, T2, T3) => R): MultiFunction3[T1, T2, T3, R] =
    macro macroToAdaptor3_impl[T1, T2, T3, R]

  def macroToAdaptor3_impl[T1, T2, T3, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3) => R]): c.Expr[MultiFunction3[T1, T2, T3, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction3[T1, T2, T3, R]](resultTree)
  }

  implicit def macroToAdaptor4[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): MultiFunction4[T1, T2, T3, T4, R] =
    macro macroToAdaptor4_impl[T1, T2, T3, T4, R]

  def macroToAdaptor4_impl[T1, T2, T3, T4, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4) => R]): c.Expr[MultiFunction4[T1, T2, T3, T4, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction4[T1, T2, T3, T4, R]](resultTree)
  }

  implicit def macroToAdaptor5[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): MultiFunction5[T1, T2, T3, T4, T5, R] =
    macro macroToAdaptor5_impl[T1, T2, T3, T4, T5, R]

  def macroToAdaptor5_impl[T1, T2, T3, T4, T5, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4, T5) => R]): c.Expr[MultiFunction5[T1, T2, T3, T4, T5, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction5[T1, T2, T3, T4, T5, R]](resultTree)
  }
}
