package scalaadaptive.core.configuration.blocks.logging

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{FileLogger, Logger}

/**
  * Created by Petr Kubat on 7/10/17.
  */
trait FileLogging extends Configuration {
  protected val logFilePath: String = "./scalaadaptive.log"
  override def createLogger: Logger =
    new FileLogger(logFilePath)
}

