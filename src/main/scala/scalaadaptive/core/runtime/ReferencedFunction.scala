package scalaadaptive.core.runtime

import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}

/**
  * Created by pk250187 on 5/21/17.
  */
class ReferencedFunction[TArgType, TReturnType](val fun: (TArgType) => TReturnType,
                                                val closureReference: ClosureNameReference,
                                                val customReference: FunctionReference,
                                                val useClosures: Boolean) {
  def this(fun: (TArgType) => TReturnType, closureReference: ClosureNameReference) =
    this(fun, closureReference, closureReference, false)

  def this(fun: (TArgType) => TReturnType, closureReference: ClosureNameReference, customReference: FunctionReference) =
    this(fun, closureReference, customReference, false)

  def reference: FunctionReference = if (useClosures) closureReference else customReference
}