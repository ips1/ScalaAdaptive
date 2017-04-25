package scalaadaptive.core.logging

import java.util.Calendar

/**
  * Created by pk250187 on 4/1/17.
  */
trait Logger {
  final def log(message: String) = {
    val now = Calendar.getInstance()
    write(s"${now.getTime}: $message")
  }
  def write(message: String)
}
