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
  val createFunctionInvoker: () => CombinedFunctionInvoker
  val createHistoryStorage: () => HistoryStorage[TMeasurement]
  val createPersistentHistoryStorage: () => Option[HistoryStorage[TMeasurement]]
  val createMeanBasedStrategy: (Logger) => SelectionStrategy[TMeasurement]
  val createInputBasedStrategy: (Logger) => SelectionStrategy[TMeasurement]
  val createGatherDataStrategy: (Logger) => SelectionStrategy[TMeasurement]
  val createEvaluationProvider: () => EvaluationProvider[TMeasurement]
  val createLogger: () => Logger
  val createIdentifierValidator: () => CustomIdentifierValidator
  val createMultiFunctionDefaultConfig: () => FunctionConfig
  val createFunctionFactory: () => FunctionFactory
  val createAnalyticsSerializer: () => AnalyticsSerializer
  val createAnalyticsCollector: () => AnalyticsCollector
  val initAdaptiveSelector: (HistoryStorage[TMeasurement], SelectionStrategy[TMeasurement],
    SelectionStrategy[TMeasurement], SelectionStrategy[TMeasurement], EvaluationProvider[TMeasurement],
    Logger) => AdaptiveSelector
}
