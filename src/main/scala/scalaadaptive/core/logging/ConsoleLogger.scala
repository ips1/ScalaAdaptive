package scalaadaptive.core.logging

/**
  * Created by Petr Kubat on 4/1/17.
  *
  * [[Logger]] that logs into the standard output.
  *
  */
class ConsoleLogger extends Logger {
  override def write(message: String): Unit = println(message)
}
