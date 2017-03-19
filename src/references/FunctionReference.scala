package references

/**
  * Created by pk250187 on 3/19/17.
  */
abstract class FunctionReference

case class MethodNameReference(name: String) extends FunctionReference
case class ClosureNameReference(name: String) extends FunctionReference
