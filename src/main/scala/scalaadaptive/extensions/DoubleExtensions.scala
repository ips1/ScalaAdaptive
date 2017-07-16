package scalaadaptive.extensions

/**
  * Created by Petr Kubat on 7/16/17.
  *
  * An object containing extension methods on the Double type.
  *
  */
object DoubleExtensions {
  /**
    * The Double type with extension methods.
    */
  implicit class RichDouble(value: Double) {
    /**
      * Checks whether the value is NaN, in such a case returns None, otherwise returns Some(value)
      * @return None if the value is NaN, Some(value) otherwise.
      */
    def asOption: Option[Double] =
      if (value.isNaN) None
      else Some(value)
  }
}
