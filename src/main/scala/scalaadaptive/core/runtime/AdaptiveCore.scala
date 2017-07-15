package scalaadaptive.core.runtime

import scalaadaptive.analytics.AnalyticsSerializer
import scalaadaptive.api.options.Storage
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.analytics.AnalyticsCollector
import scalaadaptive.core.functions.references.CustomIdentifierValidator
import scalaadaptive.core.functions.{CombinedFunctionInvoker, FunctionFactory}

/**
  * Created by pk250187 on 3/19/17.
  */
trait AdaptiveCore {
  private val defaultConfiguration = new FullHistoryTTestConfiguration
  private var currentConfiguration: Configuration = defaultConfiguration

  private def initAdaptiveSelector(configuration: Configuration): AdaptiveSelector = {
    val logger = configuration.createLogger()
    configuration.initAdaptiveSelector(configuration.createHistoryStorage(),
      configuration.createInputBasedStrategy(logger),
      configuration.createMeanBasedStrategy(logger),
      configuration.createEvaluationProvider(),
      logger)
  }

  private def initPersistentAdaptiveSelector(configuration: Configuration): Option[AdaptiveSelector] = {
    val logger = configuration.createLogger()
    configuration.createPersistentHistoryStorage().map(persistentStorage =>
      configuration.initAdaptiveSelector(persistentStorage,
        configuration.createInputBasedStrategy(logger),
        configuration.createMeanBasedStrategy(logger),
        configuration.createEvaluationProvider(),
        logger)
    )
  }

  private def initImplementations(configuration: Configuration): AdaptiveImplementations = {
    val adaptiveSelector = initAdaptiveSelector(configuration)
    new AdaptiveImplementations(
      configuration.createIdentifierValidator(),
      configuration.createMultiFunctionDefaultConfig(),
      configuration.createFunctionFactory(),
      configuration.createAnalyticsSerializer(),
      adaptiveSelector,
      initPersistentAdaptiveSelector(configuration).getOrElse(adaptiveSelector),
      configuration.createFunctionInvoker())
  }

  private var adaptiveImplementations: AdaptiveImplementations = initImplementations(defaultConfiguration)

  def getIdentifierValidator: CustomIdentifierValidator = adaptiveImplementations.identifierValidator

  def getMultiFunctionDefaults: FunctionConfig = adaptiveImplementations.multiFunctionDefaults

  def getSharedSelector: AdaptiveSelector = adaptiveImplementations.runner

  def getSharedPersistentSelector: AdaptiveSelector = adaptiveImplementations.persistentRunner

  def getFunctionFactory: FunctionFactory = adaptiveImplementations.functionFactory

  def getAnalyticsSerializer: AnalyticsSerializer = adaptiveImplementations.analyticsSerializer

  def getFunctionInvoker: CombinedFunctionInvoker = adaptiveImplementations.functionInvoker

  def createAnalytics(): AnalyticsCollector = currentConfiguration.createAnalyticsCollector()

  private def createNewSelector(): AdaptiveSelector =
    initAdaptiveSelector(currentConfiguration)

  def createLocalSelector(config: FunctionConfig): Option[AdaptiveSelector] =
    if (config.storage == Storage.Local)
      Some(createNewSelector())
    else
      None

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    adaptiveImplementations = initImplementations(currentConfiguration)
  }

  def reset(): Unit = {
    initialize(currentConfiguration)
  }

  // Default initialization
  reset()
}
