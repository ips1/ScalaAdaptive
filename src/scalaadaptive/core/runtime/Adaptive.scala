package scalaadaptive.core.runtime

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.defaults.{FullHistoryInterpolationConfiguration, FullHistoryTTestConfiguration}
import scalaadaptive.core.logging.LogManager
import scalaadaptive.core.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history._

/**
  * Created by pk250187 on 3/19/17.
  */
object Adaptive {
  private val defaultConfiguration = FullHistoryTTestConfiguration
  private var currentConfiguration: Configuration = defaultConfiguration

  private def initTracker(configuration: Configuration): FunctionRunner = {
    new RunTracker[configuration.MeasurementType](
      configuration.historyStorageFactory(),
      configuration.runSelector,
      configuration.performanceProvider,
      configuration.groupSelector,
      configuration.logger)
  }

  private def initPersistentTracker(configuration: Configuration): Option[FunctionRunner] =
    configuration.persistentHistoryStorageFactory().map(persistentStorage =>
      new RunTracker[configuration.MeasurementType](
        persistentStorage,
        configuration.runSelector,
        configuration.performanceProvider,
        configuration.groupSelector,
        configuration.logger
      )
    )

  def getIdentifierValidator: CustomIdentifierValidator = currentConfiguration.identifierValidator

  var runner: FunctionRunner = initTracker(defaultConfiguration)
  var persistentRunner: FunctionRunner = initPersistentTracker(defaultConfiguration).getOrElse(runner)

  def createRunner(): FunctionRunner =
    initTracker(currentConfiguration)

  def initialize(configuration: Configuration): Unit = {
    currentConfiguration = configuration
    LogManager.setLogger(currentConfiguration.logger)
    runner = initTracker(currentConfiguration)
    persistentRunner = initPersistentTracker(currentConfiguration).getOrElse(runner)
  }
}
