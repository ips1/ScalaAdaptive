package scalaadaptive.core.runtime

import scalaadaptive.analytics.{AnalyticsSerializer, BasicAnalyticsData, CsvAnalyticsSerializer}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.logging.LogManager
import scalaadaptive.core.functions.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history._
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionFactory}

/**
  * Created by pk250187 on 3/19/17.
  */
object AdaptiveInternal {
  private val defaultConfiguration = new FullHistoryTTestConfiguration
  private var currentConfiguration: Configuration = defaultConfiguration

  private def initOptionRunner(configuration: Configuration): AdaptiveRunner = {
    new HistoryBasedAdaptiveRunner[configuration.TMeasurement](
      configuration.createHistoryStorage(),
      configuration.createDiscreteRunSelector(),
      configuration.createContinuousRunSelector(),
      configuration.createPerformanceProvider(),
      configuration.createGroupSelector(),
      configuration.createLogger())
  }

  private def initPersistentOptionRunner(configuration: Configuration): Option[AdaptiveRunner] =
    configuration.createPersistentHistoryStorage().map(persistentStorage =>
      new HistoryBasedAdaptiveRunner[configuration.TMeasurement](
        persistentStorage,
        configuration.createDiscreteRunSelector(),
        configuration.createContinuousRunSelector(),
        configuration.createPerformanceProvider(),
        configuration.createGroupSelector(),
        configuration.createLogger()
      )
    )

  private var identifierValidator: CustomIdentifierValidator = defaultConfiguration.createIdentifierValidator()
  private var multiFunctionDefaults: FunctionConfig = defaultConfiguration.createMultiFunctionDefaultConfig()
  private var functionFactory: FunctionFactory = defaultConfiguration.createFunctionFactory()
  private var analyticsSerializer: AnalyticsSerializer = defaultConfiguration.createAnalyticsSerializer()
  private var runner: AdaptiveRunner = initOptionRunner(defaultConfiguration)
  private var persistentRunner: AdaptiveRunner = initPersistentOptionRunner(defaultConfiguration).getOrElse(runner)

  def getIdentifierValidator: CustomIdentifierValidator = identifierValidator

  def getMultiFunctionDefaults: FunctionConfig = multiFunctionDefaults

  def getSharedRunner: AdaptiveRunner = runner

  def getSharedPersistentRunner: AdaptiveRunner = persistentRunner

  def getFunctionFactory: FunctionFactory = functionFactory

  def getAnalyticsSerializer: AnalyticsSerializer = analyticsSerializer

  def createAnalytics(): AnalyticsCollector = currentConfiguration.createAnalyticsCollector()

  def createNewRunner(): AdaptiveRunner =
    initOptionRunner(currentConfiguration)

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    LogManager.setLogger(currentConfiguration.createLogger())
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
