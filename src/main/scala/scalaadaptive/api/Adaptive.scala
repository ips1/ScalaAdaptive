package scalaadaptive.api

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.runtime.{AdaptiveCore, AdaptiveInternal}

/**
  * Created by pk250187 on 6/3/17.
  */
/**
  * Global API object that allows the library user to initialize the entire framework.
  */
object Adaptive {
  val internal: AdaptiveCore = AdaptiveInternal

  /**
    * Initializes the ScalaAdaptive framework with new configuration
    * Note that some parts of the configuration (e.g. the default configuration of the MultiFunctions) will have effect
    * only on MultiFunction instances created after this call.
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
