package scalaadaptive.core.logging

/**
  * Created by pk250187 on 5/8/17.
  */
class EmptyLogger extends Logger {
  override def write(message: String): Unit = { }
}
