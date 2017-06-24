package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.{StatisticDataProvider, StatisticFunctionProvider}

/**
  * Created by pk250187 on 5/21/17.
  */
class AlwaysUseLastPolicy extends Policy {
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = (PolicyResult.UseLast, this)
}
