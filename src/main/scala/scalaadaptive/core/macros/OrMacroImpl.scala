package scalaadaptive.core.macros

import scala.reflect.macros.blackbox

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * The implementation of the or macro from all the [[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding
  * types.
  *
  */
object OrMacroImpl {
  def or_impl[TFunType, TAdaptorType](c: blackbox.Context)(fun: c.Expr[TFunType]): c.Expr[TAdaptorType] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[TAdaptorType](resultTree)
  }
}
