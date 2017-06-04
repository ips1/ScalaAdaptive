package scalaadaptive.core.configuration

import scalaadaptive.core.runtime.grouping.{GroupSelector, LogarithmGroupSelector}
import scalaadaptive.core.logging.{ConsoleLogger, Logger}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.core.functions.references.{AlphanumValidator, CustomIdentifierValidator}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedAverageRunHistory, FullRunHistory}
import scalaadaptive.core.functions.policies.AlwaysSelectPolicy
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseConfiguration extends Configuration {
  protected val num: Averageable[MeasurementType]
  override val historyStorageFactory: () => HistoryStorage[MeasurementType] = () => {
    new MapHistoryStorage[MeasurementType](key =>
      new CachedAverageRunHistory[MeasurementType](new FullRunHistory[MeasurementType](key)(num))(num)
    )
  }
  override val groupSelector: GroupSelector = new LogarithmGroupSelector
  override val logger: Logger = new ConsoleLogger
  override val identifierValidator: CustomIdentifierValidator = new AlphanumValidator

  override val multiFunctionDefaults = new FunctionConfig(Selection.Continuous, Storage.Global, None, false, new AlwaysSelectPolicy)
}
