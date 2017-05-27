package scalaadaptive.api

import scalaadaptive.core.adaptors.{Conversions}
import scalaadaptive.core.references.{CustomIdentifierValidator, CustomReference}
import scalaadaptive.core.runtime.Adaptive

/**
  * Created by pk250187 on 4/29/17.
  */
object IdentifiedFunction {
  val validator: CustomIdentifierValidator = Adaptive.getIdentifierValidator

  def apply[T, R](function: (T) => R, identifier: String) =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomReference(identifier))
}
