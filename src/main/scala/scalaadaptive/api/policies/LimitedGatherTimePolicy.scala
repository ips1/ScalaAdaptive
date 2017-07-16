package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.utilpolicies.repeatuntil._

/**
  * Created by Petr Kubat on 6/5/17.
  */
class LimitedGatherTimePolicy(val resetPeriod: Long,
                              val allowedGatherTimePerPeriod: Long,
                              val allowedSelectionTimePerPeriod: Long) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = {
    val currentTime = System.nanoTime()
    val resetTimePolicy = new RepeatUntilResetTimePolicy(PolicyResult.UseMost, currentTime + resetPeriod, this)
    val collectPolicy = new RepeatUntilTotalTimePolicy(PolicyResult.SelectNew,
      statistics.getTotalTime + allowedGatherTimePerPeriod + allowedSelectionTimePerPeriod, resetTimePolicy)
    val gatherPolicy = new RepeatUntilGatherTimePolicy(PolicyResult.GatherData,
      statistics.getTotalGatherTime + allowedGatherTimePerPeriod, collectPolicy)
    gatherPolicy.decide(statistics)
  }
}
