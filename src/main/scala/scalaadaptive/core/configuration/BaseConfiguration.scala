package scalaadaptive.core.configuration

import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.core.runtime.grouping.{GroupSelector, LogarithmGroupSelector}
import scalaadaptive.core.logging.{ConsoleLogger, Logger}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionFactory}
import scalaadaptive.core.functions.references.{AlphanumValidator, CustomIdentifierValidator}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedAverageRunHistory, FullRunHistory, LimitedRunHistory}
import scalaadaptive.core.functions.policies.AlwaysSelectPolicy
import scalaadaptive.extensions.Averageable

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseConfiguration extends Configuration {
  protected val num: Averageable[TMeasurement]
  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key =>
      new LimitedRunHistory[TMeasurement](20000,
        new CachedAverageRunHistory[TMeasurement](
          new FullRunHistory[TMeasurement](key)(num))(num))
    )
  }
  override val createGroupSelector: () => GroupSelector =
    () => new LogarithmGroupSelector
  override val createLogger: () => Logger =
    () => new ConsoleLogger
  override val createIdentifierValidator: () => CustomIdentifierValidator =
    () => new AlphanumValidator
  override val createFunctionFactory: () => FunctionFactory =
    () => new DefaultFunctionFactory
  override val createAnalyticsCollector: () => AnalyticsCollector =
    () => new BasicAnalyticsCollector
  override val createAnalyticsSerializer: () => AnalyticsSerializer =
    () => new CsvAnalyticsSerializer

  override val createMultiFunctionDefaultConfig: () => FunctionConfig =
    () => new FunctionConfig(Selection.Discrete, Storage.Global, None, false, new AlwaysSelectPolicy)
}
