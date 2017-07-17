package scalaadaptive.core.configuration.blocks.logging

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{FileLogger, Logger}

/**
  * Created by Petr Kubat on 7/10/17.
  *
  * A block that sets up a [[scalaadaptive.core.logging.FileLogger]] as the default logger.
  *
  * Path to the file can be set up by overriding the logFilePath.
  *
  */
trait FileLogging extends Configuration {
  protected val logFilePath: String = "./scalaadaptive.log"
  override def createLogger: Logger =
    new FileLogger(logFilePath)
}

