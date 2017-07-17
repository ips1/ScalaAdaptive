package scalaadaptive.core.configuration.blocks.logging

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{EmptyLogger, Logger}

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A block that sets up a [[scalaadaptive.core.logging.EmptyLogger]] as the default logger, therefore disables logging.
  *
  */
trait NoLogging extends Configuration {
  override def createLogger: Logger =
    new EmptyLogger
}
