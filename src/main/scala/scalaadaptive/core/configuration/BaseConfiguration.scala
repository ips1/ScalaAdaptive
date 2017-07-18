package scalaadaptive.core.configuration

import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.api.options.Storage
import scalaadaptive.api.policies.AlwaysSelectPolicy
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.functions.identifiers.{CustomIdentifierValidator, JavaIdentifierValidator}
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionConfig, FunctionFactory}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.historystorage.{HistoryStorage, MapHistoryStorage}
import scalaadaptive.core.runtime.history.runhistory.{FullRunHistory, LimitedRunHistory, RunHistory}
import scalaadaptive.core.runtime.invocation.{CombinedFunctionInvoker, PolicyBasedInvoker}
import scalaadaptive.core.runtime.selection.strategies.{LeastDataSelectionStrategy, SelectionStrategy}
import scalaadaptive.core.runtime.selection.{AdaptiveSelector, HistoryBasedAdaptiveSelector}
import scalaadaptive.extensions.Averageable

/**
  * Created by Petr Kubat on 4/22/17.
  *
  * A class that provides basic implementations for some of the factory methods from
  * [[scalaadaptive.core.configuration.Configuration]]. It fixes the TMeasurement type using an
  * [[scalaadaptive.extensions.Averageable]] typeclass in order to be able to use some implementations.
  *
  * In general, this extension provides construction for all the types with only one (or one main) implementation,
  * so it does not make any sense to create configuration blocks for them (see
  * [[scalaadaptive.core.configuration.blocks]]).
  *
  * It sets up the following:
  * - [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] to
  * [[scalaadaptive.core.runtime.history.historystorage.MapHistoryStorage]]
  * - [[scalaadaptive.core.runtime.history.runhistory.RunHistory]] as
  * [[scalaadaptive.core.runtime.history.runhistory.FullRunHistory]] wrapped into
  * [[scalaadaptive.core.runtime.history.runhistory.LimitedRunHistory]]
  * - [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]] to
  * [[scalaadaptive.core.functions.identifiers.JavaIdentifierValidator]]
  * - [[scalaadaptive.core.functions.FunctionFactory]] to [[scalaadaptive.core.functions.DefaultFunctionFactory]]
  * - [[scalaadaptive.analytics.AnalyticsSerializer]] to [[scalaadaptive.analytics.CsvAnalyticsSerializer]]
  * - [[scalaadaptive.core.functions.analytics.AnalyticsCollector]] to
  * [[scalaadaptive.core.functions.analytics.BasicAnalyticsCollector]]
  * - gatherDataStrategy to [[scalaadaptive.core.runtime.selection.strategies.LeastDataSelectionStrategy]]
  * - [[scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker]] to
  * [[scalaadaptive.core.runtime.invocation.PolicyBasedInvoker]]
  * - defaultFunctionConfig using basic values
  * - initAdaptiveSelector composition creating a [[scalaadaptive.core.runtime.selection.HistoryBasedAdaptiveSelector]]
  *
  */
trait BaseConfiguration extends Configuration {
  protected val num: Averageable[TMeasurement]

  /**
    * The maximum number of historical records in history of one function for one group. Used as an argument for the
    * [[scalaadaptive.core.runtime.history.runhistory.LimitedRunHistory]] class.
    */
  protected val maximumNumberOfRecords: Int = 50000

  override def createRunHistory(log: Logger, key: HistoryKey): RunHistory[TMeasurement] = {
    val innerHistoryFactory = () =>
      new FullRunHistory[TMeasurement](key)(num)
    new LimitedRunHistory[TMeasurement](log, maximumNumberOfRecords, innerHistoryFactory(), innerHistoryFactory)
  }

  override def createHistoryStorage(log: Logger): HistoryStorage[TMeasurement] =
    new MapHistoryStorage[TMeasurement](key => createRunHistory(log, key))

  override def createFunctionInvoker(log: Logger): CombinedFunctionInvoker =
    new PolicyBasedInvoker(log)

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

  override def createDefaultFunctionConfig: FunctionConfig =
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
