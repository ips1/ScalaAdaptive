package macros

import functionadaptors.FunctionAdaptor1

import scala.language.experimental.macros
import scala.reflect.macros.Context

/**
  * Created by pk250187 on 4/9/17.
  */
class AdaptiveMacrosHelper[C <: Context](val c: C) {
  import c.universe._

  def wrapTreeInAdapterConversion(funTree: c.Tree): c.Tree = {
    //val toAdaptorCallOld = Select(Ident(newTypeName("functionadaptors.Implicits")), newTermName("toAdaptor"))

    //val toAdaptorCall = TypeApply(Select(Select(Ident(newTermName("functionadaptors")), newTermName("Implicits")), newTermName("toAdaptor")), List(TypeTree(), TypeTree()))
    val toAdaptorCall = Select(Select(Ident(newTermName("functionadaptors")), newTermName("Implicits")), newTermName("toAdaptor"))

    def generateDefaultConversion(functionTree: Tree): Tree =
      Apply(
        toAdaptorCall,
        List(functionTree)
      )

    def generateExplicitConversion(functionTree: Tree, methodName: String) =
      Apply(
        toAdaptorCall,
        List(
          functionTree,
          Apply(Select(Select(Ident(newTermName("references")), newTermName("MethodNameReference")), newTermName("apply")), List(Literal(Constant(methodName))))
        )
      )

    def getMethodNameFromFunctionExpression(expression: Tree): Option[String] = expression match {
      case Select(_, _) => Some(expression.toString())
      case _ => None
    }

    def extractMethodFromCall(call: Tree): Option[String] = call match {
      case Apply(function, args) => {
        println(showRaw(function)); getMethodNameFromFunctionExpression(function)
      }
      case _ => None
    }

    def extractMethodNameFromEtaExpansion(etaExpansion: Tree): Option[String] = etaExpansion match {
      case Block(_, body) => {
        println(body); extractMethodNameFromEtaExpansion(body)
      }
      case Function(_, call) => extractMethodFromCall(call)
      case _ => None
    }

    val method = extractMethodNameFromEtaExpansion(funTree)

    val resultTree = method match {
      case Some(name) => generateExplicitConversion(funTree, name)
      case _ => generateDefaultConversion(funTree)
    }

    resultTree
  }

  def wrapTreeInAdapterConversionAndOrCall(funTree: c.Tree, types: List[Tree]): c.Tree = {
    val convertedTree = wrapTreeInAdapterConversion(funTree)
    Apply(
      TypeApply(
        Select(c.prefix.tree, newTermName("orAdaptor")),
        types
      ),
      List(convertedTree)
    )
  }
}
