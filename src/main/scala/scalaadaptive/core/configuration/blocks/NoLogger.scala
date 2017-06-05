package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.logging.{ConsoleLogger, EmptyLogger, Logger}

/**
  * Created by pk250187 on 6/5/17.
  */
trait NoLogger extends Configuration {
  override val logger: Logger = new EmptyLogger
}
