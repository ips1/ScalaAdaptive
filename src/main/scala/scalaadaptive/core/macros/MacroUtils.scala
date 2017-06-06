package scalaadaptive.core.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context
import scalaadaptive.core.functions.adaptors.FunctionAdaptor1

object MacroUtils {
  def printAst(arg: Any): Any = macro printAst_impl

  def printAst_impl(c: blackbox.Context)(arg: c.Expr[Any]): c.Expr[Any] = {
    import c.universe._
    println(showRaw(arg.tree))
    arg
  }
}