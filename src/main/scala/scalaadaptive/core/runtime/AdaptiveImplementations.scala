package scalaadaptive.core.runtime

import scalaadaptive.analytics.AnalyticsSerializer
import scalaadaptive.core.functions.{FunctionConfig, FunctionFactory}
import scalaadaptive.core.functions.identifiers.CustomIdentifierValidator
import scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker
import scalaadaptive.core.runtime.selection.AdaptiveSelector

/**
  * Created by Petr Kubat on 6/29/17.
  *
  * A simple holder class for all the implementation necessary in [[AdaptiveCore]].
  *
  */
class AdaptiveImplementations(val identifierValidator: CustomIdentifierValidator,
                              val multiFunctionDefaults: FunctionConfig,
                              val functionFactory: FunctionFactory,
                              val analyticsSerializer: AnalyticsSerializer,
                              val runner: AdaptiveSelector,
                              val persistentRunner: AdaptiveSelector,
                              val functionInvoker: CombinedFunctionInvoker)
