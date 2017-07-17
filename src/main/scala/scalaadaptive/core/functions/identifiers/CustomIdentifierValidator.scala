package scalaadaptive.core.functions.identifiers

/**
  * Created by Petr Kubat on 4/29/17.
  *
  * A validator of a [[CustomIdentifier]]. Primarily should make sure that the name can be used as a file name.
  *
  */
trait CustomIdentifierValidator {
  /** Finds out if the identifier is valid. */
  def isValidIdentifier(identifier: String): Boolean
}
