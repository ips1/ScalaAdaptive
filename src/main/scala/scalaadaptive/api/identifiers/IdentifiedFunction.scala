package scalaadaptive.api.identifiers

import scalaadaptive.api.functions._
import scalaadaptive.core.functions.adaptors.Conversions
import scalaadaptive.core.functions.identifiers.{CustomIdentifierValidator, CustomIdentifier}
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by Petr Kubat on 4/29/17.
  *
  * Object for creating adaptive functions ([[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding types)
  * with custom identifiers.
  *
  * Usage:
  * {{{
  *   val myFunction = IdentifiedFunction(f, "myFunction")
  *   val adaptedFunction = myFunction or otherFUnction
  * }}}
  *
  * If an adaptive function is created this way, it has the custom identifier included as its preferred identifier
  * instead of the method name. It will be used if not specified otherwise using the {{{asClosures}}} setting
  * on the [[scalaadaptive.api.functions.AdaptiveFunctionCommon]].
  *
  * Note that the identifier is validated using the [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]]
  * from the [[scalaadaptive.core.configuration.Configuration]] and in case of a problem, the
  * [[InvalidIdentifierException]] is thrown.
  *
  */
object IdentifiedFunction {
  private val validator: CustomIdentifierValidator = AdaptiveInternal.getIdentifierValidator

  /**
    * Creates an adapted function with one implementation and custom identifier from a function with no arguments.
    * @param function The single implementation.
    * @param identifier The function identifier.
    * @return The adaptive function.
    * @throws InvalidIdentifierException if the identifier is not valid according to the
    *                                    [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
    */
  def apply[R](function: () => R, identifier: String): AdaptiveFunction0[R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  /**
    * Creates an adapted function with one implementation and custom identifier from a function with one argument.
    * @param function The single implementation.
    * @param identifier The function identifier.
    * @return The adaptive function.
    * @throws InvalidIdentifierException if the identifier is not valid according to the
    *                                    [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
    */
  def apply[T, R](function: (T) => R, identifier: String): AdaptiveFunction1[T, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  /**
    * Creates an adapted function with one implementation and custom identifier from a function with two arguments.
    * @param function The single implementation.
    * @param identifier The function identifier.
    * @return The adaptive function.
    * @throws InvalidIdentifierException if the identifier is not valid according to the
    *                                    [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
    */
  def apply[T1, T2, R](function: (T1, T2) => R, identifier: String): AdaptiveFunction2[T1, T2, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  /**
    * Creates an adapted function with one implementation and custom identifier from a function with three arguments.
    * @param function The single implementation.
    * @param identifier The function identifier.
    * @return The adaptive function.
    * @throws InvalidIdentifierException if the identifier is not valid according to the
    *                                    [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
    */
  def apply[T1, T2, T3, R](function: (T1, T2, T3) => R, identifier: String): AdaptiveFunction3[T1, T2, T3, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  /**
    * Creates an adapted function with one implementation and custom identifier from a function with four arguments.
    * @param function The single implementation.
    * @param identifier The function identifier.
    * @return The adaptive function.
    * @throws InvalidIdentifierException if the identifier is not valid according to the
    *                                    [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
    */
  def apply[T1, T2, T3, T4, R](function: (T1, T2, T3, T4) => R, identifier: String): AdaptiveFunction4[T1, T2, T3, T4, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))

  /**
    * Creates an adapted function with one implementation and custom identifier from a function with five arguments.
    * @param function The single implementation.
    * @param identifier The function identifier.
    * @return The adaptive function.
    * @throws InvalidIdentifierException if the identifier is not valid according to the
    *                                    [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
    */
  def apply[T1, T2, T3, T4, T5, R](function: (T1, T2, T3, T4, T5) => R, identifier: String): AdaptiveFunction5[T1, T2, T3, T4, T5, R] =
    if (!validator.isValidIdentifier(identifier))
      throw new InvalidIdentifierException(s"Identifier $identifier is not valid")
    else
      Conversions.toAdaptor(function, CustomIdentifier(identifier))
}
