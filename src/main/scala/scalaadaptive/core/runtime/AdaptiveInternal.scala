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

  private def initTracker(configuration: Configuration): OptionRunner = {
    new RunTracker[configuration.MeasurementType](
      configuration.historyStorageFactory(),
      configuration.discreteRunSelector,
      configuration.continuousRunSelector,
      configuration.performanceProvider,
      configuration.groupSelector,
      configuration.logger)
  }

  private def initPersistentTracker(configuration: Configuration): Option[OptionRunner] =
    configuration.persistentHistoryStorageFactory().map(persistentStorage =>
      new RunTracker[configuration.MeasurementType](
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

  var runner: OptionRunner = initTracker(defaultConfiguration)
  var persistentRunner: OptionRunner = initPersistentTracker(defaultConfiguration).getOrElse(runner)

  def createRunner(): OptionRunner =
    initTracker(currentConfiguration)

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    LogManager.setLogger(currentConfiguration.logger)
    runner = initTracker(currentConfiguration)
    persistentRunner = initPersistentTracker(currentConfiguration).getOrElse(runner)
  }

  // Default initialization
  initialize(defaultConfiguration)
}
