package scalaadaptive.core.macros.methodnames

import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context

/**
  * Created by Petr Kubat on 4/30/17.
  *
  * Utility class that allows building parts of the ASTs.
  *
  */
class TreeBuilder[C <: blackbox.Context](val c: C) {
  import c.universe._

  private def buildTypeTree(targetNameParts: List[String]) = {
    val termNameParts = targetNameParts.map(TermName(_))
    val firstElem: Tree = Ident(termNameParts.head)

    termNameParts.tail.foldLeft(firstElem)((prev, curr) => Select(prev, curr))
  }

  /**
    * Builds an AST of a method call on a type.
    * @param targetTypeName Fully qualified name of the target type (with dots).
    * @param methodName Name of the method.
    * @param arguments ASTs of the arguments.
    * @return AST of the method call.
    */
  def buildMethodCall(targetTypeName: String, methodName: String, arguments: List[Tree]): Tree =
    buildMethodCall(targetTypeName.split('.').toList, methodName, arguments)

  /**
    * Builds an AST of a method call on a type.
    * @param targetNameParts Parts of the fully qualified name of the target type.
    * @param methodName Name of the method.
    * @param arguments ASTs of the arguments.
    * @return AST of the method call.
    */
  def buildMethodCall(targetNameParts: List[String], methodName: String, arguments: List[Tree]): Tree =
    buildMethodCall(buildTypeTree(targetNameParts), methodName, arguments)

  /**
    * Builds an AST of a method call.
    * @param targetTree AST of the target of the call.
    * @param methodName Name of the method.
    * @param arguments ASTs of the arguments.
    * @return AST of the method call.
    */
  def buildMethodCall(targetTree: Tree, methodName: String, arguments: List[Tree]): Tree = {
    val methodSelect = Select(targetTree, TermName(methodName))
    Apply(methodSelect, arguments)
  }

  /**
    * Builds an AST of a method call chain with empty arguments.
    * @param targetTree AST of the target of the calls.
    * @param methods Names of all the methods to chain.
    * @return AST of the method call chain.
    */
  def buildMethodCallChain(targetTree: Tree, methods: List[String]): Tree =
    buildMethodCallChain(targetTree, methods, methods.map(_ => List()))


  /**
    * Builds an AST of a method call chain with empty arguments.
    * @param targetTree AST of the target of the calls.
    * @param methods Names of all the methods to chain.
    * @param arguments Arguments to pass to the methods.
    * @return AST of the method call chain.
    */
  def buildMethodCallChain(targetTree: Tree, methods: List[String], arguments: List[List[Tree]]): Tree =
    methods
      .zip(arguments)
      .foldLeft(targetTree)((prev, method) => buildMethodCall(prev, method._1, method._2))
}
