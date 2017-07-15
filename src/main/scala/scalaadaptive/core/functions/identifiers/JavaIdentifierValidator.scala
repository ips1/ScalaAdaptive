package scalaadaptive.core.functions.identifiers

/**
  * Created by pk250187 on 4/29/17.
  */
class JavaIdentifierValidator extends CustomIdentifierValidator {
  override def isValidIdentifier(identifier: String): Boolean =
    identifier.toList.forall(c => c.isUnicodeIdentifierPart)
}
