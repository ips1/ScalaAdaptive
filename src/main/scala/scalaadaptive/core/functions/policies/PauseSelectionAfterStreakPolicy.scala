package scalaadaptive.core.functions.policies

import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.policies.utilpolicies.repeatuntil.RepeatUntilRunCountPolicy
import scalaadaptive.core.functions.statistics.StatisticDataProvider

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
    if (statistics.getStreakLength >= streakSize)
      (
        PolicyResult.UseLast,
        new RepeatUntilRunCountPolicy(PolicyResult.UseLast, statistics.getTotalRunCount + retryEvery, this)
      )
    else (PolicyResult.SelectNew, this)
}
