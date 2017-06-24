package scalaadaptive.core.functions.policies.builder

import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.policies.builder.conditions.Condition

/**
  * Created by pk250187 on 6/24/17.
  */
class PolicyBuilderNeedLimit(val inner: PolicyBuilderInner, val lastResult: PolicyResult) {
  val once: PolicyBuilder = new PolicyBuilder(inner.addOncePolicy(lastResult))
  def forever: Policy = inner.addForeverPolicy(lastResult).generateCyclic()
  def until(cond: Condition): PolicyBuilder = new PolicyBuilder(inner.addRepeatPolicy(lastResult, cond))
}
