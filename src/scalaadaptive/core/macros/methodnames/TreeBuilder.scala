package scalaadaptive.core.macros.methodnames

import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context

/**
  * Created by pk250187 on 4/30/17.
  */
class TreeBuilder[C <: blackbox.Context](val c: C) {
  import c.universe._

  private def buildTypeTree(targetNameParts: List[String]) = {
    val termNameParts = targetNameParts.map(TermName(_))
    val firstElem: Tree = Ident(termNameParts.head)

    termNameParts.tail.foldLeft(firstElem)((prev, curr) => Select(prev, curr))
  }

  def buildMethodCall(targetTypeName: String, methodName: String, arguments: List[Tree]): Tree =
    buildMethodCall(targetTypeName.split('.').toList, methodName, arguments)

  def buildMethodCall(targetNameParts: List[String], methodName: String, arguments: List[Tree]): Tree =
    buildMethodCall(buildTypeTree(targetNameParts), methodName, arguments)

  def buildMethodCall(targetTree: Tree, methodName: String, arguments: List[Tree]): Tree = {
    val methodSelect = Select(targetTree, TermName(methodName))
    Apply(methodSelect, arguments)
  }

  def buildMethodCallChain(targetTree: Tree, methods: List[String]): Tree =
    buildMethodCallChain(targetTree, methods, methods.map(x => List()))

  def buildMethodCallChain(targetTree: Tree, methods: List[String], arguments: List[List[Tree]]): Tree =
    methods
      .zip(arguments)
      .foldLeft(targetTree)((prev, method) => buildMethodCall(prev, method._1, method._2))
}
