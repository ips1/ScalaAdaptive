package scalaadaptive.core.runtime

import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration
import scalaadaptive.core.logging.LogManager
import scalaadaptive.core.functions.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history._
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionFactory}

/**
  * Created by pk250187 on 3/19/17.
  */
object AdaptiveInternal {
  private val defaultConfiguration = FullHistoryTTestConfiguration
  private var currentConfiguration: Configuration = defaultConfiguration

  private def initOptionRunner(configuration: Configuration): AdaptiveRunner = {
    new HistoryBasedAdaptiveRunner[configuration.MeasurementType](
      configuration.historyStorageFactory(),
      configuration.discreteRunSelector,
      configuration.continuousRunSelector,
      configuration.performanceProvider,
      configuration.groupSelector,
      configuration.logger)
  }

  private def initPersistentOptionRunner(configuration: Configuration): Option[AdaptiveRunner] =
    configuration.persistentHistoryStorageFactory().map(persistentStorage =>
      new HistoryBasedAdaptiveRunner[configuration.MeasurementType](
        persistentStorage,
        configuration.discreteRunSelector,
        configuration.continuousRunSelector,
        configuration.performanceProvider,
        configuration.groupSelector,
        configuration.logger
      )
    )

  def getIdentifierValidator: CustomIdentifierValidator = currentConfiguration.identifierValidator

  def getMultiFunctionDefaults: FunctionConfig = currentConfiguration.multiFunctionDefaults

  def getFunctionFactory: FunctionFactory = new DefaultFunctionFactory

  var runner: AdaptiveRunner = initOptionRunner(defaultConfiguration)
  var persistentRunner: AdaptiveRunner = initPersistentOptionRunner(defaultConfiguration).getOrElse(runner)

  def createRunner(): AdaptiveRunner =
    initOptionRunner(currentConfiguration)

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    LogManager.setLogger(currentConfiguration.logger)
    runner = initOptionRunner(currentConfiguration)
    persistentRunner = initPersistentOptionRunner(currentConfiguration).getOrElse(runner)
  }

  // Default initialization
  initialize(defaultConfiguration)
}
