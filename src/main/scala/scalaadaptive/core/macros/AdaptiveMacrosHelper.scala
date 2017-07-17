package scalaadaptive.core.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.core.macros.methodnames.{EtaExpansionConverter, TreeBuilder}

/**
  * Created by Petr Kubat on 4/9/17.
  *
  * A helper class to work with macros, receives the context in constructor.
  *
  */
class AdaptiveMacrosHelper[C <: blackbox.Context](val c: C) {
  import c.universe._

  val converter = new EtaExpansionConverter[c.type](c)

  /**
    * Wraps a tree containing a function into a conversion to corresponding function adaptor.
    * If the function is a literal containing eta-expansion, the method name will be extracted and applied to the
    * conversion.
    *
    * Used from the implicit type conversions in [[scalaadaptive.api.Implicits]].
    *
    * @param funTree The function tree.
    * @return The function tree wrapped in the conversion.
    */
  def wrapTreeInAdapterConversion(funTree: c.Tree): c.Tree =
    converter.convertEtaExpansionTree(funTree)

  /**
    * Wraps the tree into a conversion like [[wrapTreeInAdapterConversion]] and applies it as an argument of a
    * orAdaptiveFunction call.
    *
    * Used from the or method in [[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding types.
    *
    * @param funTree The function tree.
    * @return The function tree wrapped in the conversion and orAdaptiveFunction call.
    */
  def wrapTreeInAdapterConversionAndOrCall(funTree: c.Tree): c.Tree = {
    val convertedTree = wrapTreeInAdapterConversion(funTree)

    Apply(
      Select(c.prefix.tree, TermName("orAdaptiveFunction")),
      List(convertedTree)
    )
  }
}
