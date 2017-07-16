package scalaadaptive.core.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scalaadaptive.core.macros.methodnames.{EtaExpansionConverter, TreeBuilder}

/**
  * Created by Petr Kubat on 4/9/17.
  */
class AdaptiveMacrosHelper[C <: blackbox.Context](val c: C) {
  import c.universe._

  val converter = new EtaExpansionConverter[c.type](c)

  def wrapTreeInAdapterConversion(funTree: c.Tree): c.Tree =
    converter.convertEtaExpansionTree(funTree)

  def wrapTreeInAdapterConversionAndOrCall(funTree: c.Tree): c.Tree = {
    val convertedTree = wrapTreeInAdapterConversion(funTree)

    Apply(
      Select(c.prefix.tree, TermName("orMultiFunction")),
      List(convertedTree)
    )
  }
}
