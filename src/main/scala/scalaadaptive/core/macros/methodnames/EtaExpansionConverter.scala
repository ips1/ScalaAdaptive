package scalaadaptive.core.macros.methodnames

import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox.Context
import scalaadaptive.api.Implicits
import scalaadaptive.core.adaptors.Conversions
import scalaadaptive.core.references.MethodNameReference

/**
  * Created by pk250187 on 4/30/17.
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

  private def generateMethodReference(fullNameExpression: Tree) = {
    val methodNameReferenceType = getObjectName(MethodNameReference)
    treeBuilder.buildMethodCall(methodNameReferenceType, "apply", List(fullNameExpression))
  }

  private def generateConversionWithArguments(arguments: List[Tree]) = {
    val conversionsType = getObjectName(Conversions)
    treeBuilder.buildMethodCall(conversionsType, "toAdaptor", arguments)
  }

  private def reconstructBlock(statements: List[Tree], expression: Tree): Tree =
    Block(statements, expression)

  def convertEtaExpansionTree(tree: Tree): Tree = {
    // TODO: Validate the tree

    val newBlock: Option[Tree] = for {
      blockStatements <- extractor.extractStatements(tree)
      functionLiteral <- extractor.extractFunctionLiteral(tree)
      methodTarget <- extractor.extractMethodTarget(tree)
      methodName <- extractor.extractMethodName(tree)

      fullMethodNameExpr <- Some(generateNameExpr(methodTarget, methodName))
      methodReference <- Some(generateMethodReference(fullMethodNameExpr))
      conversion <- Some(generateConversionWithArguments(List(functionLiteral, methodReference)))
      block <- Some(reconstructBlock(blockStatements, conversion))
    } yield block

    newBlock.getOrElse(generateConversionWithArguments(List(tree)))
  }
}
