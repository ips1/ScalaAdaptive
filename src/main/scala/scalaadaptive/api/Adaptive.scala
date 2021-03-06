package scalaadaptive.api

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.runtime.{AdaptiveCore, AdaptiveInternal}

/**
  * Created by Petr Kubat on 6/3/17.
  *
  * Global API object that allows the library user to initialize the entire framework.
  *
  */
object Adaptive {
  private val internal: AdaptiveCore = AdaptiveInternal

  /**
    * Initializes the ScalaAdaptive framework with new configuration
    * Note that some parts of the configuration (e.g. the default configuration of the AdaptiveFunctions) will have effect
    * only on AdaptiveFunction instances created after this call.
    * Note that all the run history data that were stored using Storage.Global will be lost.
    * @param configuration The configuration that will be used in the framework.
    *                      For more information, see Configuration class.
    */
  def initialize(configuration: Configuration): Unit = internal.initialize(configuration)

  /**
    * Re-initializes the ScalaAdaptive framework with new configuration
    * Note that all the run history data that were stored using Storage.Global will be lost.
    */
  def reset(): Unit = internal.reset()
}
