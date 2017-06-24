package scalaadaptive.core.functions

import scalaadaptive.api.policies.Policy
import scalaadaptive.core.functions.statistics.FunctionStatistics

/**
  * Created by pk250187 on 6/19/17.
  */
class FunctionData[TArgType, TRetType](val statistics: FunctionStatistics[TArgType, TRetType],
                                       var currentPolicy: Policy)
