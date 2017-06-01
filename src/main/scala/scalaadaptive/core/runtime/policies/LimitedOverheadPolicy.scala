package scalaadaptive.core.runtime.policies

import scalaadaptive.core.runtime.policies.PolicyResult.PolicyResult
import scalaadaptive.core.runtime.policies.overhead.{RepeatUntilOverheadPolicy, RepeatUntilResetTimePolicy}
import scalaadaptive.core.runtime.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/1/17.
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
