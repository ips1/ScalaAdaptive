package scalaadaptive.api.identifiers

import scalaadaptive.core.functions.adaptors.Conversions
import scalaadaptive.core.functions.references.{CustomIdentifierValidator, CustomReference}
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by pk250187 on 4/29/17.
  */
object IdentifiedFunction {
  val validator: CustomIdentifierValidator = AdaptiveInternal.getIdentifierValidator

  def apply[T, R](function: (T) => R, identifier: String) =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomReference(identifier))
}
