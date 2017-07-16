package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.{Condition, SimpleCondition}

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * A specific modification of [[PolicyBuilder]] in a phase when a result was specified and now a limit needs to be
  * set for the result.
  *
  * If the limit gets set to forever, the builder phase gets terminated and a new [[scalaadaptive.api.policies.utilpolicies.CyclicPolicy]]
  * is generated.
  *
  */
class PolicyBuilderNeedLimit(val inner: PolicyBuilderInner, val lastResult: PolicyResult) {
  /**
    * Sets the result to be used only once, a transition to the next policy in the chain will follow.
    * A [[scalaadaptive.api.policies.utilpolicies.DoOncePolicy]] will be generated.
    */
  def once: PolicyBuilder = new PolicyBuilder(inner.addOncePolicy(lastResult))

  /**
    * Sets the result to be repeated forever, limiting the policy loop to this result.
    * A [[scalaadaptive.api.policies.utilpolicies.AlwaysDoPolicy]] will be generated.
    */
  def forever: Policy = inner.addForeverPolicy(lastResult).generateCyclic()

  /**
    * Sets the result to be used repeatedly until a [[Condition]] is fulfilled.
    * A [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilCondition]] will be generated.
    */
  def until(cond: Condition): PolicyBuilder = new PolicyBuilder(inner.addRepeatPolicy(lastResult, cond))

  /**
    * Sets the result to be used repeatedly until a [[SimpleCondition]] is fulfilled.
    * A [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilCondition]] will be generated.
    */
  def until(condition: (StatisticDataProvider) => Boolean): PolicyBuilder = until(new SimpleCondition(condition))
}
