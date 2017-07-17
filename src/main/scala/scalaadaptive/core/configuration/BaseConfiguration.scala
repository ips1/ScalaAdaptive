package scalaadaptive.core.configuration

import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.core.logging.{ConsoleLogger, Logger}
import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionConfig, FunctionFactory}
import scalaadaptive.core.functions.identifiers.{CustomIdentifierValidator, JavaIdentifierValidator}
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{CachedGroupedRunHistory, FullRunHistory, LimitedRunHistory}
import scalaadaptive.api.policies.AlwaysSelectPolicy
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.invocation.{CombinedFunctionInvoker, PolicyBasedInvoker}
import scalaadaptive.core.runtime.selection.HistoryBasedAdaptiveSelector
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, SelectionStrategy}
import scalaadaptive.core.runtime.selection.{AdaptiveSelector, HistoryBasedAdaptiveSelector}
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 4/22/17.
  */
trait BaseConfiguration extends Configuration {
  protected val num: Averageable[TMeasurement]

  protected val maximumNumberOfRecords: Int = 50000

  override def createHistoryStorage: HistoryStorage[TMeasurement] = {
    new MapHistoryStorage[TMeasurement](key => {
      val innerHistoryFactory = () =>
        new FullRunHistory[TMeasurement](key)(num)
      new LimitedRunHistory[TMeasurement](maximumNumberOfRecords, innerHistoryFactory(), innerHistoryFactory)
    })
  }
  override def createLogger: Logger =
    new ConsoleLogger
  override def createIdentifierValidator: CustomIdentifierValidator =
    new JavaIdentifierValidator
  override def createFunctionFactory: FunctionFactory =
    new DefaultFunctionFactory
  override def createAnalyticsCollector: AnalyticsCollector =
    new BasicAnalyticsCollector
  override def createAnalyticsSerializer: AnalyticsSerializer =
    new CsvAnalyticsSerializer

  override def createGatherDataStrategy(log: Logger): SelectionStrategy[TMeasurement] =
    new LeastDataSelectionStrategy[TMeasurement](log)

  override def createFunctionInvoker: CombinedFunctionInvoker =
    new PolicyBasedInvoker

  override def createMultiFunctionDefaultConfig: FunctionConfig =
    new FunctionConfig(None, Storage.Global, None, false, new AlwaysSelectPolicy)

  override def initAdaptiveSelector(historyStorage: HistoryStorage[TMeasurement],
                                    meanBasedStrategy: SelectionStrategy[TMeasurement],
                                    inputBasedStrategy: SelectionStrategy[TMeasurement],
                                    gatherDataStrategy: SelectionStrategy[TMeasurement],
                                    evaluationProvider: EvaluationProvider[TMeasurement],
                                    logger: Logger): AdaptiveSelector =
      new HistoryBasedAdaptiveSelector[TMeasurement](
        historyStorage,
        meanBasedStrategy,
        inputBasedStrategy,
        gatherDataStrategy,
        evaluationProvider,
        logger)
}
