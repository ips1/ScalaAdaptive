package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticFunctionProvider

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * A trait representing an invocation policy, a system of state machines that is used in the invocation process
  * of adaptive functions (see [[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding types) to quickly
  * decide whether to use a [[scalaadaptive.core.runtime.selection.SelectionStrategy]] to select an implementation, or
  * to take a shortcut.
  *
  * Policy gets evaluated (its decide method called) at every invocation of an adaptive function, and will produce:
  * - new policy that will be used next time
  * - a [[PolicyResult]] that will determine the invocation procedure
  *
  */
trait Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  def decide(statistics: StatisticDataProvider): (PolicyResult, Policy)
}
