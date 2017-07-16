package scalaadaptive.api

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.functions._
import scalaadaptive.core.macros.AdaptiveMacrosHelper

/**
  * Created by Petr Kubat on 3/19/17.
  */
object Implicits {
  implicit def toMultiFunction0[R](fun: () => R): AdaptiveFunction0[R] =
    macro toMultiFunction0_impl[R]

  def toMultiFunction0_impl[R](c: blackbox.Context)(fun: c.Expr[() => R]): c.Expr[AdaptiveFunction0[R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction0[R]](resultTree)
  }

  implicit def toMultiFunction1[T1, R](fun: (T1) => R): AdaptiveFunction1[T1, R] =
    macro toMultiFunction1_impl[T1, R]

  def toMultiFunction1_impl[T1, R](c: blackbox.Context)(fun: c.Expr[(T1) => R]): c.Expr[AdaptiveFunction1[T1, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction1[T1, R]](resultTree)
  }

  implicit def toMultiFunction2[T1, T2, R](fun: (T1, T2) => R): AdaptiveFunction2[T1, T2, R] =
    macro toMultiFunction2_impl[T1, T2, R]

  def toMultiFunction2_impl[T1, T2, R](c: blackbox.Context)(fun: c.Expr[(T1, T2) => R]): c.Expr[AdaptiveFunction2[T1, T2, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction2[T1, T2, R]](resultTree)
  }

  implicit def toMultiFunction3[T1, T2, T3, R](fun: (T1, T2, T3) => R): AdaptiveFunction3[T1, T2, T3, R] =
    macro toMultiFunction3_impl[T1, T2, T3, R]

  def toMultiFunction3_impl[T1, T2, T3, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3) => R]): c.Expr[AdaptiveFunction3[T1, T2, T3, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction3[T1, T2, T3, R]](resultTree)
  }

  implicit def toMultiFunction4[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): AdaptiveFunction4[T1, T2, T3, T4, R] =
    macro toMultiFunction4_impl[T1, T2, T3, T4, R]

  def toMultiFunction4_impl[T1, T2, T3, T4, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4) => R]): c.Expr[AdaptiveFunction4[T1, T2, T3, T4, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction4[T1, T2, T3, T4, R]](resultTree)
  }

  implicit def toMultiFunction5[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    macro toMultiFunction5_impl[T1, T2, T3, T4, T5, R]

  def toMultiFunction5_impl[T1, T2, T3, T4, T5, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4, T5) => R]): c.Expr[AdaptiveFunction5[T1, T2, T3, T4, T5, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction5[T1, T2, T3, T4, T5, R]](resultTree)
  }
}
