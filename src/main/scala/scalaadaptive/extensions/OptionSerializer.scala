package scalaadaptive.extensions

/**
  * Created by Petr Kubat on 5/8/17.
  *
  * Simple tool methods for serializing and deserializing the Option values.
  *
  */
object OptionSerializer {
  /**
    * Serializes Option[T] by using toString on the inner object in case of Some(value), returns empty string otherwise.
    */
  def serializeOption[T](option: Option[T]): String = option match {
    case Some(value) => value.toString
    case _ => ""
  }

  /**
    * Deserializes Option[T] using provided method if the string is not empty, returns None otherwise.
    */
  def deserizalizeOption[T](string: String, deserialization: String => T): Option[T] =
    if (string.length == 0) None
    else Some(deserialization(string))
}
