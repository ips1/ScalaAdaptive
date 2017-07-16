package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticFunctionProvider

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * A policy that will emmit [[PolicyResult.SelectNew]] replaced by [[PolicyResult.GatherData]] every
  * gatherFrequency-th time. After reaching moveOnAfter invocation, in transitions to the nextPolicy.
  *
  * It is a suitable policy for the starting phase, to gather some data quickly.
  *
  */
class StartPolicy(val gatherFrequency: Long, val moveOnAfter: Long, val nextPolicy: Policy) extends Policy {
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = {
    val next =  if (statistics.getTotalRunCount < moveOnAfter) this else nextPolicy
    val result =
      if (statistics.getTotalRunCount % gatherFrequency == 0) PolicyResult.GatherData
      else PolicyResult.SelectNew
    (result, next)
  }
}
