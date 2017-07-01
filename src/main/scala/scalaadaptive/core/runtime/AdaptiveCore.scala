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

  private def initOptionRunner(configuration: Configuration): AdaptiveSelector = {
    val logger = configuration.createLogger()
    new HistoryBasedAdaptiveSelector[configuration.TMeasurement](
      configuration.createHistoryStorage(),
      configuration.createNonPredictiveSelectionStrategy(logger),
      configuration.createPredictiveSelectionStrategy(logger),
      configuration.createPerformanceProvider(),
      logger)
  }

  private def initPersistentOptionRunner(configuration: Configuration): Option[AdaptiveSelector] = {
    val logger = configuration.createLogger()
    configuration.createPersistentHistoryStorage().map(persistentStorage =>
      new HistoryBasedAdaptiveSelector[configuration.TMeasurement](
        persistentStorage,
        configuration.createNonPredictiveSelectionStrategy(logger),
        configuration.createPredictiveSelectionStrategy(logger),
        configuration.createPerformanceProvider(),
        logger
      )
    )
  }

  private def initImplementations(configuration: Configuration): AdaptiveImplementations = {
    val optionRunner = initOptionRunner(defaultConfiguration)
    new AdaptiveImplementations(
      defaultConfiguration.createIdentifierValidator(),
      defaultConfiguration.createMultiFunctionDefaultConfig(),
      defaultConfiguration.createFunctionFactory(),
      defaultConfiguration.createAnalyticsSerializer(),
      optionRunner,
      initPersistentOptionRunner(defaultConfiguration).getOrElse(optionRunner),
      defaultConfiguration.createFunctionInvoker())
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
    initOptionRunner(currentConfiguration)

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
