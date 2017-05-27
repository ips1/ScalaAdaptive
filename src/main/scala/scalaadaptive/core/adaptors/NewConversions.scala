package scalaadaptive.core.adaptors

import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}
import scalaadaptive.core.runtime.{Adaptive, FunctionFactory, ReferencedFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
object NewConversions {
  private val defaults = Adaptive.getMultiFunctionDefaults
  private def functionFactory: FunctionFactory = Adaptive.getFunctionFactory

  private def generateClosureNameReference(closure: Any) = ClosureNameReference(closure.getClass.getTypeName)

  def toAdaptor[I, R](fun: (I) => R): NewFunctionAdaptor1[I, R] = toAdaptor(fun, generateClosureNameReference(fun))

  def toAdaptor[I, R](fun: (I) => R, reference: FunctionReference): NewFunctionAdaptor1[I, R] = fun match {
    case adaptor: NewFunctionAdaptor1[I, R] => adaptor
    case _ =>
      new NewFunctionAdaptor1[I, R](
        functionFactory.createFunction[I, R](new ReferencedFunction[I, R](fun, generateClosureNameReference(fun), reference)),
        functionFactory
      )
  }
}
