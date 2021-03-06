package scalaadaptive.core.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context
import scalaadaptive.core.functions.adaptors.FunctionAdaptor1

/**
  * Simple utilities to be used to help working with macros
  */
object MacroUtils {
  /** A simple macro that prints the AST of its argument and returns it. Suitable for analyzing ASTs. */
  def printAst(arg: Any): Any = macro printAst_impl

  def printAst_impl(c: blackbox.Context)(arg: c.Expr[Any]): c.Expr[Any] = {
    import c.universe._
    println(showRaw(arg.tree))
    arg
  }
}