package scalaadaptive.core.macros.methodnames

import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context
import scalaadaptive.api.Implicits
import scalaadaptive.core.functions.adaptors.Conversions
import scalaadaptive.core.functions.identifiers.MethodNameIdentifier

/**
  * Created by Petr Kubat on 4/30/17.
  *
  * Main class that performs the wrapping of the function literal into the conversion call along with attempted
  * eta-expasion method name extraction.
  *
  */
class EtaExpansionConverter[C <: blackbox.Context](val c: C) {
  import c.universe._
  val extractor = new EtaExpansionExtractor[c.type](c)
  val treeBuilder = new TreeBuilder[c.type](c)

  private def getTypeName(obj: Any) =
    obj.getClass.getName

  private def getObjectName(obj: Any) = {
    val name = getTypeName(obj)
    // Removing trailing $ for object / singleton classes
    if (name.last == '$')
      name.substring(0, math.max(name.length - 1, 0))
    else
      name
  }

  private def generateNameExpr(methodTarget: Tree, methodName: Name) = {
    val methods = List("getClass", "getName", "$plus")
    val arguments = List(List(), List(), List(Literal(Constant(s".$methodName"))))
    treeBuilder.buildMethodCallChain(methodTarget, methods, arguments)
  }

  private def generateMethodIdentifier(fullNameExpression: Tree) = {
    val methodNameIdentifierType = getObjectName(MethodNameIdentifier)
    treeBuilder.buildMethodCall(methodNameIdentifierType, "apply", List(fullNameExpression))
  }

  private def generateConversionWithArguments(arguments: List[Tree]) = {
    val conversionsType = getObjectName(Conversions)
    treeBuilder.buildMethodCall(conversionsType, "toAdaptor", arguments)
  }

  private def reconstructBlock(statements: List[Tree], expression: Tree): Tree =
    Block(statements, expression)

  /**
    * Wraps the function tree into a toAdaptor conversion. Attempts to extract the method name in case of eta-expansion
    * function litral. If succeeds in the extraction, the method name is passed into the conversion.
    * @param tree The function AST.
    * @return The AST wrapped in a toAdaptor conversion that can contain the extracted method name.
    */
  def convertEtaExpansionTree(tree: Tree): Tree = {
    val newBlock: Option[Tree] = for {
      // If the following extractions succeed, we consider the function to be eta-expansion and generate the conversion
      blockStatements <- extractor.extractStatements(tree)
      functionLiteral <- extractor.extractFunctionLiteral(tree)
      methodTarget <- extractor.extractMethodTarget(tree)
      methodName <- extractor.extractMethodName(tree)

      fullMethodNameExpr <- Some(generateNameExpr(methodTarget, methodName))
      methodIdentifier <- Some(generateMethodIdentifier(fullMethodNameExpr))
      conversion <- Some(generateConversionWithArguments(List(functionLiteral, methodIdentifier)))
      block <- Some(reconstructBlock(blockStatements, conversion))
    } yield block

    // Fallback variant in case that the eta-expansion extraction or conversion generation failed
    newBlock.getOrElse(generateConversionWithArguments(List(tree)))
  }
}
