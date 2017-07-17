package scalaadaptive.core.configuration
import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionConfig, FunctionFactory}
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.functions.identifiers.CustomIdentifierValidator
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker
import scalaadaptive.core.runtime.selection.AdaptiveSelector
import scalaadaptive.core.runtime.selection.strategies.SelectionStrategy

/**
  * Created by Petr Kubat on 3/26/17.
  */
trait Configuration {
  type TMeasurement
  def createFunctionInvoker: CombinedFunctionInvoker
  def createHistoryStorage: HistoryStorage[TMeasurement]
  def createPersistentHistoryStorage: Option[HistoryStorage[TMeasurement]]
  def createMeanBasedStrategy(log: Logger): SelectionStrategy[TMeasurement]
  def createInputBasedStrategy(log: Logger): SelectionStrategy[TMeasurement]
  def createGatherDataStrategy(log: Logger): SelectionStrategy[TMeasurement]
  def createEvaluationProvider: EvaluationProvider[TMeasurement]
  def createLogger: Logger
  def createIdentifierValidator: CustomIdentifierValidator
  def createMultiFunctionDefaultConfig: FunctionConfig
  def createFunctionFactory: FunctionFactory
  def createAnalyticsSerializer: AnalyticsSerializer
  def createAnalyticsCollector: AnalyticsCollector
  def initAdaptiveSelector(historyStorage: HistoryStorage[TMeasurement],
                           meanBasedStrategy: SelectionStrategy[TMeasurement],
                           inputBasedStrategy: SelectionStrategy[TMeasurement],
                           gatherDataStrategy: SelectionStrategy[TMeasurement],
                           evaluationProvider: EvaluationProvider[TMeasurement],
                           logger: Logger): AdaptiveSelector
}
