package logging

/**
  * Created by pk250187 on 4/1/17.
  */
class ConsoleLogger extends Logger {
  override def log(message: String): Unit = println(message)
}
