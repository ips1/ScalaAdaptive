package scalaadaptive.core.runtime

import scalaadaptive.analytics.{AnalyticsSerializer, BasicAnalyticsData, CsvAnalyticsSerializer}
import scalaadaptive.api.options.Storage
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.functions.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history._
import scalaadaptive.core.functions.{CombinedFunctionInvoker, DefaultFunctionFactory, FunctionFactory}

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

  private var identifierValidator: CustomIdentifierValidator = defaultConfiguration.createIdentifierValidator()
  private var multiFunctionDefaults: FunctionConfig = defaultConfiguration.createMultiFunctionDefaultConfig()
  private var functionFactory: FunctionFactory = defaultConfiguration.createFunctionFactory()
  private var analyticsSerializer: AnalyticsSerializer = defaultConfiguration.createAnalyticsSerializer()
  private var runner: AdaptiveSelector = initOptionRunner(defaultConfiguration)
  private var persistentRunner: AdaptiveSelector = initPersistentOptionRunner(defaultConfiguration).getOrElse(runner)
  private var functionInvoker: CombinedFunctionInvoker = defaultConfiguration.createFunctionInvoker()

  def getIdentifierValidator: CustomIdentifierValidator = identifierValidator

  def getMultiFunctionDefaults: FunctionConfig = multiFunctionDefaults

  def getSharedRunner: AdaptiveSelector = runner

  def getSharedPersistentRunner: AdaptiveSelector = persistentRunner

  def getFunctionFactory: FunctionFactory = functionFactory

  def getAnalyticsSerializer: AnalyticsSerializer = analyticsSerializer

  def getFunctionInvoker: CombinedFunctionInvoker = functionInvoker

  def createAnalytics(): AnalyticsCollector = currentConfiguration.createAnalyticsCollector()

  def createNewSelector(): AdaptiveSelector =
    initOptionRunner(currentConfiguration)

  def createLocalSelector(config: FunctionConfig): Option[AdaptiveSelector] =
    if (config.storage == Storage.Local)
      Some(createNewSelector())
    else
      None

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    runner = initOptionRunner(currentConfiguration)
    persistentRunner = initPersistentOptionRunner(currentConfiguration).getOrElse(runner)
    identifierValidator = currentConfiguration.createIdentifierValidator()
    multiFunctionDefaults = currentConfiguration.createMultiFunctionDefaultConfig()
    functionFactory = currentConfiguration.createFunctionFactory()
    analyticsSerializer = currentConfiguration.createAnalyticsSerializer()
    functionInvoker = currentConfiguration.createFunctionInvoker()
  }

  def reset(): Unit = {
    initialize(currentConfiguration)
  }

  // Default initialization
  reset()
}
