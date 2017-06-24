package scalaadaptive.core.functions.policies.utilpolicies.repeatuntil

import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/5/17.
  *
  * A policy that will keep producing given result until a condition based on the StatisticsDataProvider is
  * fulfilled. Whenever this happens, it immediately lets the nextPolicy do the decision.
  *
  */
class RepeatUntilCondition(val result: PolicyResult,
                           val condition: (StatisticDataProvider) => Boolean,
                           val nextPolicy: Policy) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) =
    if (!condition(statistics))
      (result, this)
    else
      nextPolicy.decide(statistics)
}