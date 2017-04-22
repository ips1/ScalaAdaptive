package runtime

import configuration.{Configuration, TimeMeasurementAverageConfiguration}
import runtime.history._

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

  def createRunner(): FunctionRunner =
    initTracker(defaultConfiguration)

  lazy val runner = createRunner()
}
