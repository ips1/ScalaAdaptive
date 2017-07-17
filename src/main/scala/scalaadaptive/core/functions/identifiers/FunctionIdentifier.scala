package scalaadaptive.core.functions.identifiers

/**
  * Created by Petr Kubat on 3/19/17.
  *
  * An identifier of a function. Can be based on three features, as described in detail in the original thesis text.
  *
  */
abstract class FunctionIdentifier

/** An identifier based on a method name */
case class MethodNameIdentifier(name: String) extends FunctionIdentifier
/** An identifier based on the name of a function closur */
case class ClosureIdentifier(name: String) extends FunctionIdentifier
/** A custom identifier provided by the user */
case class CustomIdentifier(name: String) extends FunctionIdentifier
