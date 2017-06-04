package scalaadaptive.core.functions.references

/**
  * Created by pk250187 on 4/29/17.
  */
class AlphanumValidator extends CustomIdentifierValidator {
  override def isValidIdentifier(identifier: String): Boolean =
    identifier.toList.forall(c => c.isLetterOrDigit)
}
