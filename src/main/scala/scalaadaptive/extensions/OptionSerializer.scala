package scalaadaptive.extensions

/**
  * Created by Petr Kubat on 5/8/17.
  */
object OptionSerializer {
  def serializeOption[T](option: Option[T]): String = option match {
    case Some(value) => value.toString
    case _ => ""
  }

  def deserizalizeOption[T](string: String, deserialization: String => T): Option[T] =
    if (string.length == 0) None
    else Some(deserialization(string))
}
