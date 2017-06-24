package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.utilpolicies.DoOncePolicy
import scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilRunCountPolicy

/**
  * Created by pk250187 on 6/12/17.
  */
class PauseSelectionAfterStreakPolicy(val streakSize: Int, val retryEvery: Int) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) =
    if (statistics.getStreakLength >= streakSize) {
      val retryPolicy = new DoOncePolicy(PolicyResult.SelectNew, this)
      (
        PolicyResult.UseLast,
        new RepeatUntilRunCountPolicy(PolicyResult.UseLast, statistics.getTotalRunCount + retryEvery, retryPolicy)
      )
    }
    else (PolicyResult.SelectNew, this)
}
