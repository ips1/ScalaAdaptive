package scalaadaptive.api.policies

import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.statistics.StatisticFunctionProvider

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * A policy that will emmit the [[PolicyResult.UseLast]] forever.
  *
  */
class AlwaysUseLastPolicy extends Policy {
  override def decide(statistics: StatisticDataProvider): (PolicyResult, Policy) = (PolicyResult.UseLast, this)
}
