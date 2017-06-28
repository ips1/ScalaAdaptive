package scalaadaptive.api

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.adaptors._
import scalaadaptive.core.macros.AdaptiveMacrosHelper

/**
  * Created by pk250187 on 3/19/17.
  */
object Implicits {
  implicit def toMultiFunction0[R](fun: () => R): MultiFunction0[R] =
    macro toMultiFunction0_impl[R]

  def toMultiFunction0_impl[R](c: blackbox.Context)(fun: c.Expr[() => R]): c.Expr[MultiFunction0[R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction0[R]](resultTree)
  }

  implicit def toMultiFunction1[T1, R](fun: (T1) => R): MultiFunction1[T1, R] =
    macro toMultiFunction1_impl[T1, R]

  def toMultiFunction1_impl[T1, R](c: blackbox.Context)(fun: c.Expr[(T1) => R]): c.Expr[MultiFunction1[T1, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction1[T1, R]](resultTree)
  }

  implicit def toMultiFunction2[T1, T2, R](fun: (T1, T2) => R): MultiFunction2[T1, T2, R] =
    macro toMultiFunction2_impl[T1, T2, R]

  def toMultiFunction2_impl[T1, T2, R](c: blackbox.Context)(fun: c.Expr[(T1, T2) => R]): c.Expr[MultiFunction2[T1, T2, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction2[T1, T2, R]](resultTree)
  }

  implicit def toMultiFunction3[T1, T2, T3, R](fun: (T1, T2, T3) => R): MultiFunction3[T1, T2, T3, R] =
    macro toMultiFunction3_impl[T1, T2, T3, R]

  def toMultiFunction3_impl[T1, T2, T3, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3) => R]): c.Expr[MultiFunction3[T1, T2, T3, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction3[T1, T2, T3, R]](resultTree)
  }

  implicit def toMultiFunction4[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): MultiFunction4[T1, T2, T3, T4, R] =
    macro toMultiFunction4_impl[T1, T2, T3, T4, R]

  def toMultiFunction4_impl[T1, T2, T3, T4, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4) => R]): c.Expr[MultiFunction4[T1, T2, T3, T4, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction4[T1, T2, T3, T4, R]](resultTree)
  }

  implicit def toMultiFunction5[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): MultiFunction5[T1, T2, T3, T4, T5, R] =
    macro toMultiFunction5_impl[T1, T2, T3, T4, T5, R]

  def toMultiFunction5_impl[T1, T2, T3, T4, T5, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4, T5) => R]): c.Expr[MultiFunction5[T1, T2, T3, T4, T5, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[MultiFunction5[T1, T2, T3, T4, T5, R]](resultTree)
  }
}
