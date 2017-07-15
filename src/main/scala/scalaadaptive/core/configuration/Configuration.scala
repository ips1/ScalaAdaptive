package scalaadaptive.core.configuration
import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.core.functions.{CombinedFunctionInvoker, DefaultFunctionFactory, FunctionFactory}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.functions.identifiers.CustomIdentifierValidator
import scalaadaptive.core.runtime.AdaptiveSelector
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.selection.SelectionStrategy

/**
  * Created by pk250187 on 3/26/17.
  */
trait Configuration {
  type TMeasurement
  val createFunctionInvoker: () => CombinedFunctionInvoker
  val createHistoryStorage: () => HistoryStorage[TMeasurement]
  val createPersistentHistoryStorage: () => Option[HistoryStorage[TMeasurement]]
  val createMeanBasedStrategy: (Logger) => SelectionStrategy[TMeasurement]
  val createInputBasedStrategy: (Logger) => SelectionStrategy[TMeasurement]
  val createEvaluationProvider: () => EvaluationProvider[TMeasurement]
  val createLogger: () => Logger
  val createIdentifierValidator: () => CustomIdentifierValidator
  val createMultiFunctionDefaultConfig: () => FunctionConfig
  val createFunctionFactory: () => FunctionFactory
  val createAnalyticsSerializer: () => AnalyticsSerializer
  val createAnalyticsCollector: () => AnalyticsCollector
  val initAdaptiveSelector: (HistoryStorage[TMeasurement], SelectionStrategy[TMeasurement],
    SelectionStrategy[TMeasurement], EvaluationProvider[TMeasurement], Logger) => AdaptiveSelector
}
