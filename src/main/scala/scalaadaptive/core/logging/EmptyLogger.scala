package scalaadaptive.core.logging

/**
  * Created by Petr Kubat on 5/8/17.
  */
class EmptyLogger extends Logger {
  override def write(message: String): Unit = { }
}
