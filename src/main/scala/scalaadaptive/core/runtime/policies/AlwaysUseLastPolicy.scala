package scalaadaptive.core.runtime.policies

import scalaadaptive.core.runtime.policies.PolicyResult.PolicyResult
import scalaadaptive.core.runtime.statistics.{StatisticDataProvider, StatisticFunctionProvider}

/**
  * Created by pk250187 on 5/21/17.
  */
class AlwaysUseLastPolicy extends Policy {
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = (PolicyResult.UseLast, this)
}
