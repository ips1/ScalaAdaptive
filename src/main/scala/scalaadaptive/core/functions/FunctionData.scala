package scalaadaptive.core.functions

import scalaadaptive.api.policies.Policy
import scalaadaptive.core.functions.statistics.FunctionStatistics

/**
  * Created by Petr Kubat on 6/19/17.
  *
  * Class representing the mutable state of a [[CombinedFunction]].
  *
  * @param statistics Statistics of the function.
  * @param currentPolicy Currently active policy.
  *
  */
class FunctionData[TArgType, TRetType](val statistics: FunctionStatistics[TArgType, TRetType],
                                       var currentPolicy: Policy)
