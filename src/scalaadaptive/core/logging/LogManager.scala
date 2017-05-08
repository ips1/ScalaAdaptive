package scalaadaptive.core.logging

/**
  * Created by pk250187 on 5/8/17.
  */
object LogManager {
  private var logger: Logger = new EmptyLogger

  def setLogger(newLogger: Logger): Unit = {
    logger = newLogger
  }

  def getLogger: Logger = logger
}
