package scalaadaptive.core.configuration

import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.core.logging.{ConsoleLogger, Logger}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.functions.{CombinedFunctionInvoker, DefaultFunctionFactory, FunctionFactory, PolicyBasedInvoker}
import scalaadaptive.core.functions.identifiers.{JavaIdentifierValidator, CustomIdentifierValidator}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedAverageRunHistory, CachedGroupedRunHistory, FullRunHistory, LimitedRunHistory}
import scalaadaptive.api.policies.AlwaysSelectPolicy
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.{AdaptiveSelector, HistoryBasedAdaptiveSelector}
import scalaadaptive.core.runtime.selection.SelectionStrategy
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 4/22/17.
  */
trait BaseConfiguration extends Configuration {
  protected val num: Averageable[TMeasurement]

  protected val maximumNumberOfRecords: Int = 50000

  override val createHistoryStorage: () => HistoryStorage[TMeasurement] = () => {
    new MapHistoryStorage[TMeasurement](key => {
      val innerHistoryFactory = () =>
        new FullRunHistory[TMeasurement](key)(num)
      new LimitedRunHistory[TMeasurement](maximumNumberOfRecords, innerHistoryFactory(), innerHistoryFactory)
    })
  }
  override val createLogger: () => Logger =
    () => new ConsoleLogger
  override val createIdentifierValidator: () => CustomIdentifierValidator =
    () => new JavaIdentifierValidator
  override val createFunctionFactory: () => FunctionFactory =
    () => new DefaultFunctionFactory
  override val createAnalyticsCollector: () => AnalyticsCollector =
    () => new BasicAnalyticsCollector
  override val createAnalyticsSerializer: () => AnalyticsSerializer =
    () => new CsvAnalyticsSerializer

  override val createFunctionInvoker: () => CombinedFunctionInvoker =
    () => new PolicyBasedInvoker

  override val createMultiFunctionDefaultConfig: () => FunctionConfig =
    () => new FunctionConfig(None, Storage.Global, None, false, new AlwaysSelectPolicy)

  override val initAdaptiveSelector: (HistoryStorage[TMeasurement], SelectionStrategy[TMeasurement],
    SelectionStrategy[TMeasurement], EvaluationProvider[TMeasurement], Logger) => AdaptiveSelector =
    (historyStorage, meanBasedStrategy, inputBasedStrategy, evaluationProvider, logger) =>
      new HistoryBasedAdaptiveSelector[TMeasurement](
        historyStorage,
        meanBasedStrategy,
        inputBasedStrategy,
        evaluationProvider,
        logger)
}
