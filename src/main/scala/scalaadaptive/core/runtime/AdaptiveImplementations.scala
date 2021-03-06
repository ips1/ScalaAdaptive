package scalaadaptive.core.runtime

import scalaadaptive.analytics.AnalyticsSerializer
import scalaadaptive.core.functions.{FunctionConfig, FunctionFactory}
import scalaadaptive.core.functions.identifiers.CustomIdentifierValidator
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker
import scalaadaptive.core.runtime.selection.AdaptiveSelector

/**
  * Created by Petr Kubat on 6/29/17.
  *
  * A simple holder class for all the implementation necessary in [[AdaptiveCore]].
  *
  */
class AdaptiveImplementations(val logger: Logger,
                              val identifierValidator: CustomIdentifierValidator,
                              val adaptiveFunctionDefaults: FunctionConfig,
                              val functionFactory: FunctionFactory,
                              val analyticsSerializer: AnalyticsSerializer,
                              val selector: AdaptiveSelector,
                              val persistentSelector: AdaptiveSelector,
                              val functionInvoker: CombinedFunctionInvoker)
