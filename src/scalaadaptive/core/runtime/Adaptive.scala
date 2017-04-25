package scalaadaptive.core.runtime

import scalaadaptive.core.configuration.{Configuration, TimeMeasurementAverageConfiguration}
import scalaadaptive.core.runtime.history._

/**
  * Created by pk250187 on 3/19/17.
  */
object Adaptive {
  private val defaultConfiguration = TimeMeasurementAverageConfiguration

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

  def createRunner(): FunctionRunner =
    initTracker(defaultConfiguration)

  lazy val runner: FunctionRunner = createRunner()
  lazy val persistentRunner: FunctionRunner = initPersistentTracker(defaultConfiguration) match {
    case Some(r) => r
    case _ => runner
  }
}
