package scalaadaptive.core.configuration
import scalaadaptive.core.functions.FunctionFactory
import scalaadaptive.core.functions.adaptors.FunctionConfig
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
  val createDiscreteRunSelector: () => RunSelector[TMeasurement]
  val createContinuousRunSelector: () => RunSelector[TMeasurement]
  val createPerformanceProvider: () => EvaluationProvider[TMeasurement]
  val createGroupSelector: () => GroupSelector
  val createLogger: () => Logger
  val createIdentifierValidator: () => CustomIdentifierValidator
  val createMultiFunctionDefaultConfig: () => FunctionConfig
  val createFunctionFactory: () => FunctionFactory
}
