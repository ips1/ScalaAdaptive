package scalaadaptive.core.functions.policies.builder

import scalaadaptive.core.functions.policies.Policy
import scalaadaptive.core.functions.policies.builder.conditions.Condition

/**
  * Created by pk250187 on 6/24/17.
  */
class PolicyBuilderNeedNextPolicy(val inner: PolicyBuilderInner, val condition: Condition) {
  def goTo(policy: Policy): PolicyBuilder = {
    new PolicyBuilder(inner.addConditionalPolicy(policy, condition))
  }
}
