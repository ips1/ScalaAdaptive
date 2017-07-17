package scalaadaptive.core.configuration
import scalaadaptive.analytics.AnalyticsSerializer
import scalaadaptive.core.functions.analytics.AnalyticsCollector
import scalaadaptive.core.functions.identifiers.CustomIdentifierValidator
import scalaadaptive.core.functions.{FunctionConfig, FunctionFactory}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker
import scalaadaptive.core.runtime.selection.AdaptiveSelector
import scalaadaptive.core.runtime.selection.strategies.SelectionStrategy

/**
  * Created by Petr Kubat on 3/26/17.
  *
  * The main configuration of the ScalaAdaptive framework. Holds factory methods for all the main components that
  * the framework is built from. The [[scalaadaptive.core.runtime.AdaptiveInternal]] uses these factory methods
  * to compose the components and then holds them at runtime, accessible to the API.
  *
  * By implementing the factory methods, custom implementation of all the components can be provided and the framework
  * can be extended this way.
  *
  */
trait Configuration {
  /**
    * The abstract type of the evaluation data gathered from function runs.
    * Note that the [[scalaadaptive.core.runtime.AdaptiveInternal]] and the API does not depend on this type, so any
    * type can be used and even changed at runtime. The only limitation is that corresponding implementations of
    * traits that depend on the type (e.g. [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] and
    * [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]]) have to be provided by the factory methods
    * for the new type.
    */
  type TMeasurement

  /**
    * The [[scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker]] factory method.
    * @return The function invoker.
    */
  def createFunctionInvoker: CombinedFunctionInvoker

  /**
    * The [[scalaadaptive.core.runtime.history.runhistory.RunHistory]] factory method. It can be used by the
    * [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] to create new histories.
    * @param key Key of the newly created history.
    * @return The empty history.
    */
  def createRunHistory(key: HistoryKey): RunHistory[TMeasurement]

  /**
    * The [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] factory method - the storage created
    * by this method will be used in a global shared variant as a global storage
    * (see [[scalaadaptive.api.options.Storage.Global]]), and in local instances as a local storage
    * (see [[scalaadaptive.api.options.Storage.Local]]).
    * @return The storage.
    */
  def createHistoryStorage: HistoryStorage[TMeasurement]

  /**
    * The [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] factory method - the storage created
    * by this method will be used as a global persistent storage (see [[scalaadaptive.api.options.Storage.Persistent]]).
    * Note that the implementation might use an internal storage created using [[createHistoryStorage]].
    * @return The storage.
    */
  def createPersistentHistoryStorage: Option[HistoryStorage[TMeasurement]]

  /**
    * The [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] factory method - the strategy created
    * by this method will be used as a mean based strategy (see [[scalaadaptive.api.options.Selection.MeanBased]]).
    * @return The strategy.
    */
  def createMeanBasedStrategy(log: Logger): SelectionStrategy[TMeasurement]

  /**
    * The [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] factory method - the strategy created
    * by this method will be used as a input based strategy (see [[scalaadaptive.api.options.Selection.InputBased]]).
    * @return The strategy.
    */
  def createInputBasedStrategy(log: Logger): SelectionStrategy[TMeasurement]

  /**
    * The [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] factory method - the strategy created
    * by this method will be used to gather data, so typically
    * [[scalaadaptive.core.runtime.selection.strategies.LeastDataSelectionStrategy]] or similar should be used.
    * @return The strategy.
    */
  def createGatherDataStrategy(log: Logger): SelectionStrategy[TMeasurement]

  /**
    * The [[scalaadaptive.core.runtime.history.evaluation.EvaluationProvider]] factory method.
    * @return The evaluation provider.
    */
  def createEvaluationProvider: EvaluationProvider[TMeasurement]

  /**
    * The [[scalaadaptive.core.logging.Logger]] factory method. Note that only one instance of the logger will be
    * created and passed around to all the modules that require it.
    * @return The logger.
    */
  def createLogger: Logger

  /**
    * The [[scalaadaptive.core.functions.identifiers.CustomIdentifierValidator]] factory method. Creates a validator
    * that will be used by [[scalaadaptive.api.identifiers.IdentifiedFunction]] upon creating a new
    * [[scalaadaptive.core.functions.identifiers.CustomIdentifier]].
    * @return The validator.
    */
  def createIdentifierValidator: CustomIdentifierValidator

  /**
    * The factory method for creating default [[scalaadaptive.core.functions.FunctionConfig]]. All newly created
    * [[scalaadaptive.core.functions.CombinedFunction]] instances should have this configuration (actually
    * depends on the [[scalaadaptive.core.functions.FunctionFactory]] implementation).
    * @return The config.
    */
  def createDefaultFunctionConfig: FunctionConfig

  /**
    * The [[scalaadaptive.core.functions.FunctionFactory]] factory method. Creates a factory that will be used
    * to create new instances of [[scalaadaptive.core.functions.CombinedFunction]].
    * @return The factory.
    */
  def createFunctionFactory: FunctionFactory

  /**
    * The [[scalaadaptive.analytics.AnalyticsSerializer]] factory method.
    * @return The serializer.
    */
  def createAnalyticsSerializer: AnalyticsSerializer

  /**
    * The [[scalaadaptive.core.functions.analytics.AnalyticsCollector]] factory method.
    * @return The collector.
    */
  def createAnalyticsCollector: AnalyticsCollector

  /**
    * The method that should compose a new instance of [[scalaadaptive.core.runtime.selection.AdaptiveSelector]] using
    * all the possible modules that it depends on.
    * @param historyStorage The history storage that it will use.
    * @param meanBasedStrategy The mean based strategy that it will use with
    *                          [[scalaadaptive.api.options.Selection.MeanBased]] setting.
    * @param inputBasedStrategy The input based strategy that it will use with
    *                           [[scalaadaptive.api.options.Selection.InputBased]] setting.
    * @param gatherDataStrategy The strategy that will be used for gathering data.
    * @param evaluationProvider The evaluation provider that will be used to evaluate function runs.
    * @param logger The logger.
    * @return The constructed instance of [[scalaadaptive.core.runtime.selection.AdaptiveSelector]].
    */
  def initAdaptiveSelector(historyStorage: HistoryStorage[TMeasurement],
                           meanBasedStrategy: SelectionStrategy[TMeasurement],
                           inputBasedStrategy: SelectionStrategy[TMeasurement],
                           gatherDataStrategy: SelectionStrategy[TMeasurement],
                           evaluationProvider: EvaluationProvider[TMeasurement],
                           logger: Logger): AdaptiveSelector
}
