package scalaadaptive.core.macros.methodnames

import scala.reflect.macros.blackbox.Context

/**
  * Created by pk250187 on 4/30/17.
  */
class EtaExpansionExtractor[C <: Context](val c: C) {
  import c.universe._

  def extractStatements(block: Tree): Option[List[Tree]] = block match {
    case Block(statements, Function(_, _)) => Some(statements)
    case _ => None
  }

  def extractFunctionLiteral(block: Tree): Option[Tree] = block match {
    case Block(_, function) => Some(function)
    case _ => None
  }

  def extractFunctionBody(block: Tree): Option[Tree] = extractFunctionLiteral(block) match {
    case Some(Function(_, body)) => Some(body)
    case _ => None
  }

  def extractMethodTarget(block: Tree): Option[Tree] = extractFunctionBody(block) match {
    case Some(Apply(Select(target, _), _)) => Some(target)
    case _ => None
  }

  def extractMethodName(block: Tree): Option[Name] = extractFunctionBody(block) match {
    case Some(Apply(Select(_, name), _)) => Some(name)
    case _ => None
  }

//  def extractMethodTargetAndNameFromFunctionBodyTree(call: Tree): Option[(Tree, Name)] = call match {
//    case Apply(Select(target, method), _) => Some((target, method))
//    case _ => None
//  }
}