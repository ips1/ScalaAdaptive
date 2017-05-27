package scalaadaptive.core.runtime.policies

import scalaadaptive.core.runtime.policies.PolicyResult.PolicyResult
import scalaadaptive.core.runtime.statistics.{StatisticDataProvider, StatisticFunctionProvider}

/**
  * Created by pk250187 on 5/21/17.
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
