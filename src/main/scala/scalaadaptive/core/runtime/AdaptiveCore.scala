package scalaadaptive.core.runtime

import scalaadaptive.analytics.{AnalyticsSerializer, BasicAnalyticsData, CsvAnalyticsSerializer}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.functions.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history._
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionFactory}

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
      configuration.createGroupSelector(),
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
        configuration.createGroupSelector(),
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

  def getIdentifierValidator: CustomIdentifierValidator = identifierValidator

  def getMultiFunctionDefaults: FunctionConfig = multiFunctionDefaults

  def getSharedRunner: AdaptiveSelector = runner

  def getSharedPersistentRunner: AdaptiveSelector = persistentRunner

  def getFunctionFactory: FunctionFactory = functionFactory

  def getAnalyticsSerializer: AnalyticsSerializer = analyticsSerializer

  def createAnalytics(): AnalyticsCollector = currentConfiguration.createAnalyticsCollector()

  def createNewRunner(): AdaptiveSelector =
    initOptionRunner(currentConfiguration)

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    runner = initOptionRunner(currentConfiguration)
    persistentRunner = initPersistentOptionRunner(currentConfiguration).getOrElse(runner)
    identifierValidator = currentConfiguration.createIdentifierValidator()
    multiFunctionDefaults = currentConfiguration.createMultiFunctionDefaultConfig()
    functionFactory = currentConfiguration.createFunctionFactory()
    analyticsSerializer = currentConfiguration.createAnalyticsSerializer()
  }

  def reset(): Unit = {
    initialize(currentConfiguration)
  }

  // Default initialization
  reset()
}
