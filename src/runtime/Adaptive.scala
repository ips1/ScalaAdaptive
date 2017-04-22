package runtime

import configuration.{Configuration, TimeMeasurementDefaultConfiguration}
import runtime.history._

/**
  * Created by pk250187 on 3/19/17.
  */
object Adaptive {
  private val defaultConfiguration = TimeMeasurementDefaultConfiguration

  private def initTracker(configuration: Configuration): FunctionRunner = {
    new RunTracker[configuration.MeasurementType](
      configuration.globalHistoryStorage,
      configuration.runSelector,
      configuration.performanceProvider,
      configuration.groupSelector,
      configuration.logger)
  }

  def createStorage: HistoryStorage[defaultConfiguration.MeasurementType] =
    defaultConfiguration.historyStorageFactory()

  lazy val tracker = initTracker(defaultConfiguration)
}
