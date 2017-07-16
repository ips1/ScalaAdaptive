package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.{Condition, SimpleCondition}

/**
  * Created by Petr Kubat on 6/24/17.
  */
class PolicyBuilderNeedLimit(val inner: PolicyBuilderInner, val lastResult: PolicyResult) {
  val once: PolicyBuilder = new PolicyBuilder(inner.addOncePolicy(lastResult))
  def forever: Policy = inner.addForeverPolicy(lastResult).generateCyclic()
  def until(cond: Condition): PolicyBuilder = new PolicyBuilder(inner.addRepeatPolicy(lastResult, cond))
  def until(cond: (StatisticDataProvider) => Boolean): PolicyBuilder = until(new SimpleCondition(cond))
}
