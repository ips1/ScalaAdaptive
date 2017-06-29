package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticFunctionProvider

/**
  * Created by pk250187 on 5/21/17.
  */
trait Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  def decide(statistics: StatisticDataProvider): (PolicyResult, Policy)
}