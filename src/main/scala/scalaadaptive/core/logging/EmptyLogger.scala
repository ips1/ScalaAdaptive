package scalaadaptive.core.logging

/**
  * Created by Petr Kubat on 5/8/17.
  *
  * [[Logger]] that does nothing. It is used primarily to save logging overhead in an environment where we don't need
  * to see the logs.
  *
  * This is the default implementation used in the [[scalaadaptive.core.configuration.defaults.DefaultConfiguration]].
  *
  */
class EmptyLogger extends Logger {
  override def write(message: String): Unit = { }
}
