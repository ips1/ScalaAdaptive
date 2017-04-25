package scalaadaptive.core.macros

import scalaadaptive.api.functionadaptors.FunctionAdaptor1

import scala.language.experimental.macros
import scala.reflect.macros.Context

/**
  * Created by pk250187 on 4/9/17.
  */
class AdaptiveMacrosHelper[C <: Context](val c: C) {
  import c.universe._

  def wrapTreeInAdapterConversion(funTree: c.Tree): c.Tree = {
    //val toAdaptorCallOld = Select(Ident(newTypeName("scalaadaptive.api.functionadaptors.Implicits")), newTermName("toAdaptor"))

    //val toAdaptorCall = TypeApply(Select(Select(Ident(newTermName("scalaadaptive.api.functionadaptors")), newTermName("Implicits")), newTermName("toAdaptor")), List(TypeTree(), TypeTree()))
    val toAdaptorCall =
      Select(
        Select(
          Select(
            Ident(
              TermName("scalaadaptive")
            ),
            TermName("api")
          ),
          TermName("Implicits")
        ),
        TermName("toAdaptor")
      )

    def generateDefaultConversion(functionTree: Tree): Tree =
      Apply(
        toAdaptorCall,
        List(functionTree)
      )

    def generateExplicitConversion(functionTree: Tree, methodNameTree: Tree) =
      Apply(
        toAdaptorCall,
        List(
          functionTree,
          Apply(
            Select(
              Select(
                Select(
                  Select(
                    Ident(
                      TermName("scalaadaptive")
                    ),
                    TermName("core")
                  ),
                  TermName("references")
                ),
                TermName("MethodNameReference")
              ),
              TermName("apply")
            ),
            List(
              methodNameTree
            )
          )
        )
      )

    def generateNameFromTypeAndMehod(typeTree: Tree, method: Name): Option[Tree] = {
//      val typeNameTerm = TermName(typeName.toString)
//      val methodNameTerm = TermName(method.toString)
//      Some(q"""(classOf[${typeNameTerm.decodedName.toString}].getTypeName + ".${methodNameTerm.decodedName.toString}")""")



      Some(
        Apply(
          Select(
            Apply(
              Select(
                Apply(
                  Select(
                    typeTree,
                    TermName("getClass")
                  ),
                  List()
                ),
                TermName("getName")
              ),
              List()
            ),
            TermName("$plus")
          ),
          List(Literal(Constant(s".$method")))
        )
      )
//      None
    }

    def getMethodNameFromFunctionExpression(expression: Tree): Option[Tree] = expression match {
      case Select(typeTree, methodTree) => generateNameFromTypeAndMehod(typeTree, methodTree)
        // TODO: Cover the following: (+ support genericity?)
        // Warning:scalac: TypeApply(Select(This(TypeName("CovarianceContravariance")), TermName("method2")), List(TypeTree().setOriginal(Ident(TypeName("T")))))
      case _ =>
        println("------------------------")
        println(showRaw(expression))
        None
    }

    def extractMethodFromCall(call: Tree): Option[Tree] = call match {
      case Apply(function, args) =>
        println(showRaw(function))
        getMethodNameFromFunctionExpression(function)
      case _ => None
    }

    def extractMethodNameFromEtaExpansion(etaExpansion: Tree): Option[Tree] = etaExpansion match {
      case Block(_, body) =>
        println(body)
        extractMethodNameFromEtaExpansion(body)
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

  def wrapTreeInAdapterConversionAndOrCall(funTree: c.Tree): c.Tree = {
    val convertedTree = wrapTreeInAdapterConversion(funTree)
    Apply(
      Select(c.prefix.tree, TermName("orAdaptor")),
      List(convertedTree)
    )
  }
}
