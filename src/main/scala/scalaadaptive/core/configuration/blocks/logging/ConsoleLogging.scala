package scalaadaptive.core.configuration.blocks.logging

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{ConsoleLogger, Logger}

/**
  * Created by Petr Kubat on 7/3/17.
  *
  * A block that sets up a [[scalaadaptive.core.logging.ConsoleLogger]] as the default logger.
  *
  */
trait ConsoleLogging extends Configuration {
  override def createLogger: Logger =
    new ConsoleLogger
}

