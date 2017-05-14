package scalaadaptive.core.adaptors

import scalaadaptive.core.references.{ClosureNameReference, FunctionReference}

/**
  * Created by pk250187 on 3/19/17.
  */
class RunOption[TFunction](val function: TFunction,
                           val closureReference: ClosureNameReference,
                           val reference: FunctionReference) {
  def this(function: TFunction, closureReference: ClosureNameReference) =
    this(function, closureReference, closureReference)
}