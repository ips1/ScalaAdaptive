package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.utilpolicies.repeatuntil.{RepeatUntilOverheadPolicy, RepeatUntilResetTimePolicy}

/**
  * Created by Petr Kubat on 6/1/17.
  *
  * A policy that works in loops and for each loop will emmit:
  * - [[PolicyResult.SelectNew]] until [[StatisticDataProvider.getTotalOverheadTime]] reaches allowedOverheadPerPeriod
  * - then [[PolicyResult.UseMost]] until the resetPeriod elapses
  * - afterwards, the loop is reset
  *
  */
class LimitedOverheadPolicy(val resetPeriod: Long,
                            val allowedOverheadPerPeriod: Long) extends Policy {
  /**
    * Decides on the action to take and on the policy to use in the next decision
    *
    * @param statistics The statistics of previous runs that the decision should be based on
    * @return The result and the policy that is to be used in the next decision
    */
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = {
    val currentTime = System.nanoTime()
    val resetTimePolicy = new RepeatUntilResetTimePolicy(PolicyResult.UseMost, currentTime + resetPeriod, this)
    val overheadPolicy = new RepeatUntilOverheadPolicy(PolicyResult.SelectNew, statistics.getTotalOverheadTime + allowedOverheadPerPeriod, resetTimePolicy)
    overheadPolicy.decide(statistics)
  }
}
