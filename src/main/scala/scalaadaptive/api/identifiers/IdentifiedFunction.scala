package scalaadaptive.api.identifiers

import scalaadaptive.api.functions._
import scalaadaptive.core.functions.adaptors.Conversions
import scalaadaptive.core.functions.identifiers.{CustomIdentifierValidator, CustomIdentifier}
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by Petr Kubat on 4/29/17.
  */
object IdentifiedFunction {
  val validator: CustomIdentifierValidator = AdaptiveInternal.getIdentifierValidator

  def apply[R](function: () => R, identifier: String): AdaptiveFunction0[R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  def apply[T, R](function: (T) => R, identifier: String): AdaptiveFunction1[T, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  def apply[T1, T2, R](function: (T1, T2) => R, identifier: String): AdaptiveFunction2[T1, T2, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  def apply[T1, T2, T3, R](function: (T1, T2, T3) => R, identifier: String): AdaptiveFunction3[T1, T2, T3, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  def apply[T1, T2, T3, T4, R](function: (T1, T2, T3, T4) => R, identifier: String): AdaptiveFunction4[T1, T2, T3, T4, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  def apply[T1, T2, T3, T4, T5, R](function: (T1, T2, T3, T4, T5) => R, identifier: String): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))
}
