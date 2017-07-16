package scalaadaptive.extensions

/**
  * Created by Petr Kubat on 7/16/17.
  */
object DoubleExtensions {
  implicit class RichDouble(value: Double) {
    def asOption: Option[Double] =
      if (value.isNaN) None
      else Some(value)
  }
}
