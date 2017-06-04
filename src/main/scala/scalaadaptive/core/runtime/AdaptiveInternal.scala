package scalaadaptive.core.runtime

import scalaadaptive.core.adaptors.FunctionConfig
import scalaadaptive.core.configuration.{Configuration}
import scalaadaptive.core.configuration.defaults.{FullHistoryTTestConfiguration}
import scalaadaptive.core.logging.LogManager
import scalaadaptive.core.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history._

/**
  * Created by pk250187 on 3/19/17.
  */
object AdaptiveInternal {
  private val defaultConfiguration = FullHistoryTTestConfiguration
  private var currentConfiguration: Configuration = defaultConfiguration

  private def initOptionRunner(configuration: Configuration): OptionRunner = {
    new HistoryBasedOptionRunner[configuration.MeasurementType](
      configuration.historyStorageFactory(),
      configuration.discreteRunSelector,
      configuration.continuousRunSelector,
      configuration.performanceProvider,
      configuration.groupSelector,
      configuration.logger)
  }

  private def initPersistentOptionRunner(configuration: Configuration): Option[OptionRunner] =
    configuration.persistentHistoryStorageFactory().map(persistentStorage =>
      new HistoryBasedOptionRunner[configuration.MeasurementType](
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

  var runner: OptionRunner = initOptionRunner(defaultConfiguration)
  var persistentRunner: OptionRunner = initPersistentOptionRunner(defaultConfiguration).getOrElse(runner)

  def createRunner(): OptionRunner =
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
