package scalaadaptive.core.macros

import scala.reflect.macros.blackbox

/**
  * Created by pk250187 on 6/5/17.
  */
object OrMacroImpl {
  def or_impl[TFunType, TAdaptorType](c: blackbox.Context)(fun: c.Expr[TFunType]): c.Expr[TAdaptorType] = {
    val resultTree = new AdaptiveMacrosHelper[c.type](c)
      .wrapTreeInAdapterConversionAndOrCall(fun.tree)
    c.Expr[TAdaptorType](resultTree)
  }
}
