package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult

/**
  * Created by Petr Kubat on 6/1/17.
  */
class StopSelectingWhenDecidedPolicy(val minRuns: Long = 50, val minPercentage: Double = 0.75) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = {
    if (statistics.getTotalRunCount > minRuns &&
      (statistics.getLastRunCount.toDouble / statistics.getTotalRunCount > minPercentage))
      (PolicyResult.UseLast, new AlwaysUseLastPolicy)
    else (PolicyResult.SelectNew, this)
  }
}
