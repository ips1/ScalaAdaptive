package scalaadaptive.api.policies.utilpolicies

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult

/**
  * Created by pk250187 on 6/23/17.
  *
  * General type of cyclic policy. It will build the entire policy chain upon decision (the factory functions
  * can work with the statistics at the moment of chaining and should correctly set the nextPolicy of all the
  * policies involved in the chain) and end it with cyclic transfer back to itself.
  *
  */
class CyclicPolicy(val policyFactories: Seq[(Policy, StatisticDataProvider) => Policy]) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = {
    // Build the policy chain from the last to the first one
    val firstPolicy =
      policyFactories.foldRight[Policy](this)((factory, lastPolicy) => factory(lastPolicy, statistics))
    firstPolicy.decide(statistics)
  }
}
