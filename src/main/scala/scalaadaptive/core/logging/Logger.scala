package scalaadaptive.core.logging

import java.util.Calendar

/**
  * Created by Petr Kubat on 4/1/17.
  *
  * A simple trait for logging run information to a certain target.
  * Implementations should provide only the write method, which should not be used by the caller.
  *
  */
trait Logger {
  /**
    * Logs the message.
    * The message gets timestamped and then delegated to the write method.
    * @param message The message to be logged.
    */
  final def log(message: String): Unit = {
    val now = Calendar.getInstance()
    write(s"${now.getTime}: $message")
  }

  /** Writes a message to the target log */
  protected def write(message: String)
}
