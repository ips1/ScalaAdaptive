package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.Policy
import scalaadaptive.api.policies.builder.conditions.Condition

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * A specific modification of [[PolicyBuilder]] in a phase when a condition for a conditional transition was specified
  * and the target policy needs to be added.
  *
  */
class PolicyBuilderNeedNextPolicy(val inner: PolicyBuilderInner, val condition: Condition) {
  /**
    * Adds the target [[Policy]] of the conditional transition.
    */
  def goTo(policy: Policy): PolicyBuilder = {
    new PolicyBuilder(inner.addConditionalPolicy(policy, condition))
  }
}
