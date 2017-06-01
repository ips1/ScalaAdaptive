package scalaadaptive.core.configuration

import scalaadaptive.core.grouping.{GroupSelector, LogarithmGroupSelector}
import scalaadaptive.core.logging.{ConsoleLogger, Logger}
import scalaadaptive.core.adaptors.AdaptorConfig
import scalaadaptive.core.options.{Selection, Storage}
import scalaadaptive.core.references.{AlphanumValidator, CustomIdentifierValidator}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedAverageRunHistory, FullRunHistory}
import scalaadaptive.core.runtime.policies.AlwaysSelectPolicy
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

  override val multiFunctionDefaults = new AdaptorConfig(Selection.Continuous, Storage.Global, None, false, new AlwaysSelectPolicy)
}
