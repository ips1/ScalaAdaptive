package scalaadaptive.api

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.api.functions._
import scalaadaptive.core.macros.AdaptiveMacrosHelper

/**
  * Created by Petr Kubat on 3/19/17.
  *
  * The main API object containing all the implicit conversions required for creating adaptive functions (see
  * [[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding types).
  *
  * This object has to be imported in the scope if the default conversions from Scala functions to adaptive functions
  * should take effect. It is the only supported way of creating the adaptive function instances.
  *
  * Note that the implicits in this object are compile-time macros. The _impl methods which provide macro
  * implementations have to be public, they should, however, never be used directly.
  *
  */
object Implicits {
  /**
    * Implicit conversion of function with no arguments to adaptive function with this single implementation.
    */
  implicit def toAdaptiveFunction0[R](fun: () => R): AdaptiveFunction0[R] =
    macro toAdaptiveFunction0_impl[R]

  def toAdaptiveFunction0_impl[R](c: blackbox.Context)(fun: c.Expr[() => R]): c.Expr[AdaptiveFunction0[R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction0[R]](resultTree)
  }

  /**
    * Implicit conversion of function with one argument to adaptive function with this single implementation.
    */
  implicit def toAdaptiveFunction1[T1, R](fun: (T1) => R): AdaptiveFunction1[T1, R] =
    macro toAdaptiveFunction1_impl[T1, R]

  def toAdaptiveFunction1_impl[T1, R](c: blackbox.Context)(fun: c.Expr[(T1) => R]): c.Expr[AdaptiveFunction1[T1, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction1[T1, R]](resultTree)
  }

  /**
    * Implicit conversion of function with two arguments to adaptive function with this single implementation.
    */
  implicit def toAdaptiveFunction2[T1, T2, R](fun: (T1, T2) => R): AdaptiveFunction2[T1, T2, R] =
    macro toAdaptiveFunction2_impl[T1, T2, R]

  def toAdaptiveFunction2_impl[T1, T2, R](c: blackbox.Context)(fun: c.Expr[(T1, T2) => R]): c.Expr[AdaptiveFunction2[T1, T2, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction2[T1, T2, R]](resultTree)
  }

  /**
    * Implicit conversion of function with three arguments to adaptive function with this single implementation.
    */
  implicit def toAdaptiveFunction3[T1, T2, T3, R](fun: (T1, T2, T3) => R): AdaptiveFunction3[T1, T2, T3, R] =
    macro toAdaptiveFunction3_impl[T1, T2, T3, R]

  def toAdaptiveFunction3_impl[T1, T2, T3, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3) => R]): c.Expr[AdaptiveFunction3[T1, T2, T3, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction3[T1, T2, T3, R]](resultTree)
  }

  /**
    * Implicit conversion of function with four arguments to adaptive function with this single implementation.
    */
  implicit def toAdaptiveFunction4[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R): AdaptiveFunction4[T1, T2, T3, T4, R] =
    macro toAdaptiveFunction4_impl[T1, T2, T3, T4, R]

  def toAdaptiveFunction4_impl[T1, T2, T3, T4, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4) => R]): c.Expr[AdaptiveFunction4[T1, T2, T3, T4, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction4[T1, T2, T3, T4, R]](resultTree)
  }

  /**
    * Implicit conversion of function with five arguments to adaptive function with this single implementation.
    */
  implicit def toAdaptiveFunction5[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    macro toAdaptiveFunction5_impl[T1, T2, T3, T4, T5, R]

  def toAdaptiveFunction5_impl[T1, T2, T3, T4, T5, R](c: blackbox.Context)(fun: c.Expr[(T1, T2, T3, T4, T5) => R]): c.Expr[AdaptiveFunction5[T1, T2, T3, T4, T5, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)
    c.Expr[AdaptiveFunction5[T1, T2, T3, T4, T5, R]](resultTree)
  }
}
