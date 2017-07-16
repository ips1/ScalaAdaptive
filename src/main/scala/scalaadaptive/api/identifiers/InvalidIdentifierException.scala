package scalaadaptive.api.identifiers

/**
  * Created by Petr Kubat on 4/29/17.
  *
  * An exception thrown when creating an adaptive function using [[IdentifiedFunction]] with an invalid identifier
  * according to the [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]].
  *
  */
class InvalidIdentifierException(message: String) extends Exception(message)
