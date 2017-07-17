package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{ConsoleLogger, EmptyLogger, Logger}

/**
  * Created by Petr Kubat on 6/5/17.
  */
trait NoLogging extends Configuration {
  override def createLogger: Logger =
    new EmptyLogger
}
