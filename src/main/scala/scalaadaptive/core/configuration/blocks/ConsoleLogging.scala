package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{ConsoleLogger, Logger}

/**
  * Created by pk250187 on 7/3/17.
  */
trait ConsoleLogging extends Configuration {
  override val createLogger: () => Logger =
    () => new ConsoleLogger
}

