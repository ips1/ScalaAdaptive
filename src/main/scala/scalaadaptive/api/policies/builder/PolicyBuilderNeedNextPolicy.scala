package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.Policy
import scalaadaptive.api.policies.builder.conditions.Condition

/**
  * Created by Petr Kubat on 6/24/17.
  */
class PolicyBuilderNeedNextPolicy(val inner: PolicyBuilderInner, val condition: Condition) {
  def goTo(policy: Policy): PolicyBuilder = {
    new PolicyBuilder(inner.addConditionalPolicy(policy, condition))
  }
}
