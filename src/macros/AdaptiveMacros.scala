package macros

import functionadaptors.{FunctionAdaptor1, Implicits}

import scala.language.experimental.macros
import scala.reflect.macros.Context

object AdaptiveMacros {
  implicit def macroToAdaptor[I, R](fun: (I) => R): FunctionAdaptor1[I, R] = macro toAdaptor_impl[I, R]

  def printAst(arg: Any): Any = macro printAst_impl

  def printAst_impl(c: Context)(arg: c.Expr[Any]): c.Expr[Any] = {
    import c.universe._
    println(showRaw(arg.tree))
    arg
  }

  def toAdaptor_impl[I, R](c: Context)(fun: c.Expr[(I) => R]): c.Expr[FunctionAdaptor1[I, R]] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c).wrapTreeInAdapterConversion(fun.tree)

    val x = c.Expr[FunctionAdaptor1[I, R]](resultTree)
    println(x)
    x
  }
}