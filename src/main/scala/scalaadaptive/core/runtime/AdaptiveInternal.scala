package scalaadaptive.core.runtime

import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 6/6/17.
  *
  * A singleton holding all the data from [[AdaptiveCore]], accessible to all other modules at any moment of execution.
  *
  */
object AdaptiveInternal extends AdaptiveCore
