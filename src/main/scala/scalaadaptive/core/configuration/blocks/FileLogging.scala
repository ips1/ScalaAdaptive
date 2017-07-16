package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{ConsoleLogger, FileLogger, Logger}

/**
  * Created by Petr Kubat on 7/10/17.
  */
trait FileLogging extends Configuration {
  protected val logFilePath: String = "./scalaadaptive.log"
  override val createLogger: () => Logger =
    () => new FileLogger(logFilePath)
}

