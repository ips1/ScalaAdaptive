package scalaadaptive.core.configuration
import scalaadaptive.core.adaptors.FunctionConfig
import scalaadaptive.core.grouping.GroupSelector
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.performance.MeasurementProvider
import scalaadaptive.core.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/26/17.
  */
trait Configuration {
  type MeasurementType
  val historyStorageFactory: () => HistoryStorage[MeasurementType]
  val persistentHistoryStorageFactory: () => Option[HistoryStorage[MeasurementType]]
  val discreteRunSelector: RunSelector[MeasurementType]
  val continuousRunSelector: RunSelector[MeasurementType]
  val performanceProvider: MeasurementProvider[MeasurementType]
  val groupSelector: GroupSelector
  val logger: Logger
  val identifierValidator: CustomIdentifierValidator
  val multiFunctionDefaults: FunctionConfig
}
