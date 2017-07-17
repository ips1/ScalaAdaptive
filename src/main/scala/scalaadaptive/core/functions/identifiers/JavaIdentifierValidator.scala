package scalaadaptive.core.functions.identifiers

/**
  * Created by Petr Kubat on 4/29/17.
  *
  * [[CustomIdentifierValidator]] implementation that imposes the same requirements as Java identifiers.
  *
  */
class JavaIdentifierValidator extends CustomIdentifierValidator {
  override def isValidIdentifier(identifier: String): Boolean =
    identifier.toList.forall(c => c.isUnicodeIdentifierPart)
}
