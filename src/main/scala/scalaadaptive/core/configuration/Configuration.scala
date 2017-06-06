package scalaadaptive.core.configuration
import scalaadaptive.analytics.{AnalyticsSerializer, CsvAnalyticsSerializer}
import scalaadaptive.core.functions.{DefaultFunctionFactory, FunctionFactory}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.analytics.{AnalyticsCollector, BasicAnalyticsCollector}
import scalaadaptive.core.runtime.grouping.GroupSelector
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.functions.references.CustomIdentifierValidator
import scalaadaptive.core.runtime.history.evaluation.EvaluationProvider
import scalaadaptive.core.runtime.history.historystorage.HistoryStorage
import scalaadaptive.core.runtime.selection.RunSelector

/**
  * Created by pk250187 on 3/26/17.
  */
trait Configuration {
  type TMeasurement
  val createHistoryStorage: () => HistoryStorage[TMeasurement]
  val createPersistentHistoryStorage: () => Option[HistoryStorage[TMeasurement]]
  val createDiscreteRunSelector: (Logger) => RunSelector[TMeasurement]
  val createContinuousRunSelector: (Logger) => RunSelector[TMeasurement]
  val createPerformanceProvider: () => EvaluationProvider[TMeasurement]
  val createGroupSelector: () => GroupSelector
  val createLogger: () => Logger
  val createIdentifierValidator: () => CustomIdentifierValidator
  val createMultiFunctionDefaultConfig: () => FunctionConfig
  val createFunctionFactory: () => FunctionFactory
  val createAnalyticsSerializer: () => AnalyticsSerializer
  val createAnalyticsCollector: () => AnalyticsCollector
}
