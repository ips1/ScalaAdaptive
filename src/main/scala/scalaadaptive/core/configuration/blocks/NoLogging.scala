package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{ConsoleLogger, EmptyLogger, Logger}

/**
  * Created by pk250187 on 6/5/17.
  */
trait NoLogging extends Configuration {
  override val createLogger: () => Logger =
    () => new EmptyLogger
}