package scalaadaptive.core.runtime

import scalaadaptive.analytics.AnalyticsSerializer
import scalaadaptive.api.options.Storage
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.DefaultConfiguration
import scalaadaptive.core.functions.analytics.AnalyticsCollector
import scalaadaptive.core.functions.identifiers.CustomIdentifierValidator
import scalaadaptive.core.functions.{FunctionConfig, FunctionFactory}
import scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker
import scalaadaptive.core.runtime.selection.AdaptiveSelector

/**
  * Created by Petr Kubat on 3/19/17.
  *
  * The core of the ScalaAdaptive framework - a type that holds all instances of the important modules and can create
  * them using a [[scalaadaptive.core.configuration.Configuration]] instance.
  *
  * Requires a default [[scalaadaptive.core.configuration.Configuration]] from its implementation, which will be used
  * at construction time to initialize the implementations with default values.
  *
  * In current usage in ScalaAdaptive, it is used via a singleton [[AdaptiveInternal]] object. The functionality is
  * extracted into the trait in order to be able to use it multiple times not only in singletons in the future.
  *
  */
trait AdaptiveCore {
  /** The configuration that is used for first initialization */
  protected val defaultConfiguration: Configuration = new DefaultConfiguration
  private var currentConfiguration: Configuration = defaultConfiguration

  private def initAdaptiveSelector(configuration: Configuration): AdaptiveSelector = {
    val logger = configuration.createLogger
    configuration.initAdaptiveSelector(configuration.createHistoryStorage,
      configuration.createMeanBasedStrategy(logger),
      configuration.createInputBasedStrategy(logger),
      configuration.createGatherDataStrategy(logger),
      configuration.createEvaluationProvider,
      logger)
  }

  private def initPersistentAdaptiveSelector(configuration: Configuration): Option[AdaptiveSelector] = {
    val logger = configuration.createLogger
    configuration.createPersistentHistoryStorage.map(persistentStorage =>
      configuration.initAdaptiveSelector(persistentStorage,
        configuration.createMeanBasedStrategy(logger),
        configuration.createInputBasedStrategy(logger),
        configuration.createGatherDataStrategy(logger),
        configuration.createEvaluationProvider,
        logger)
    )
  }

  private def initImplementations(configuration: Configuration): AdaptiveImplementations = {
    val adaptiveSelector = initAdaptiveSelector(configuration)
    new AdaptiveImplementations(
      configuration.createIdentifierValidator,
      configuration.createMultiFunctionDefaultConfig,
      configuration.createFunctionFactory,
      configuration.createAnalyticsSerializer,
      adaptiveSelector,
      initPersistentAdaptiveSelector(configuration).getOrElse(adaptiveSelector),
      configuration.createFunctionInvoker)
  }

  private var adaptiveImplementations: AdaptiveImplementations = initImplementations(defaultConfiguration)

  /** Retrieves the implementation of [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]]. */
  def getIdentifierValidator: CustomIdentifierValidator = adaptiveImplementations.identifierValidator

  /** Retrieves the default [[scalaadaptive.core.functions.FunctionConfig]]. */
  def getMultiFunctionDefaults: FunctionConfig = adaptiveImplementations.multiFunctionDefaults

  /** Retrieves the shared implementation of [[scalaadaptive.core.runtime.selection.AdaptiveSelector]] working with
    * global storage. */
  def getSharedSelector: AdaptiveSelector = adaptiveImplementations.runner

  /** Retrieves the shared implementation of [[scalaadaptive.core.runtime.selection.AdaptiveSelector]] working with
    * persistent storage (note that it returns the same instance as [[getSharedSelector]] if persistent storage is not
    * supported by the configuration). */
  def getSharedPersistentSelector: AdaptiveSelector = adaptiveImplementations.persistentRunner

  /** Retrieves the implementation of [[scalaadaptive.core.functions.FunctionFactory]]. */
  def getFunctionFactory: FunctionFactory = adaptiveImplementations.functionFactory

  /** Retrieves the implementation of [[scalaadaptive.analytics.AnalyticsSerializer]]. */
  def getAnalyticsSerializer: AnalyticsSerializer = adaptiveImplementations.analyticsSerializer

  /** Retrieves the implementation of [[CombinedFunctionInvoker]]. */
  def getFunctionInvoker: CombinedFunctionInvoker = adaptiveImplementations.functionInvoker

  /** Retrieves the implementation of [[scalaadaptive.core.functions.analytics.AnalyticsCollector]]. */
  def createAnalytics(): AnalyticsCollector = currentConfiguration.createAnalyticsCollector

  private def createNewSelector(): AdaptiveSelector =
    initAdaptiveSelector(currentConfiguration)

  /** Creates a new instance of [[scalaadaptive.core.runtime.selection.AdaptiveSelector]] to hold local history storage.
    * Returns None in case the function configuration does not allow the local history storage. */
  def createLocalSelector(config: FunctionConfig): Option[AdaptiveSelector] =
    if (config.storage == Storage.Local)
      Some(createNewSelector())
    else
      None


  /**
    * Initializes the framework with a configuration, throwing away the former implementations including all the data
    * stored (in run histories etc.).
    * @param configuration The new configuration to use for instance creation.
    */
  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    adaptiveImplementations = initImplementations(currentConfiguration)
  }

  /**
    * Reinitializes the framework using current configuration.
    */
  def reset(): Unit = {
    initialize(currentConfiguration)
  }

  // Default initialization
  reset()
}
