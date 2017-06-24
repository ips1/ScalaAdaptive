package scalaadaptive.core.functions.policies.builder

import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.policies.builder.conditions.Condition

class PolicyBuilder(val inner: PolicyBuilderInner) {
  def andThen(result: PolicyResult): PolicyBuilderNeedLimit =
    new PolicyBuilderNeedLimit(inner, result)
  def andThenGoTo(policy: Policy): Policy =
    inner.addPolicy(policy).generateCyclic()
  def andThenRepeat: Policy =
    inner.generateCyclic()
  def andThenIf(condition: Condition): PolicyBuilderNeedNextPolicy =
    new PolicyBuilderNeedNextPolicy(inner, condition)
}
