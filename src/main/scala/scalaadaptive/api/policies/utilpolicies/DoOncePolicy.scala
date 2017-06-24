package scalaadaptive.api.policies.utilpolicies

import scalaadaptive.api.policies.Policy
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/12/17.
  *
  * A policy that will produce giver result once and then move to the nextPolicy.
  *
  */
class DoOncePolicy(val result: PolicyResult, val nextPolicy: Policy) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) =
    (result, nextPolicy)
}
