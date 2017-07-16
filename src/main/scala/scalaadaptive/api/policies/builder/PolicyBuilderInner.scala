package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.Condition
import scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilCondition
import scalaadaptive.api.policies.utilpolicies.{AlwaysDoPolicy, CyclicPolicy, DoOncePolicy, IfPolicy}

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * The inner representation of partially complete cyclic policy within a [[PolicyBuilder]].
  *
  * The policy steps of the loop are held in form of factories that should be invoked at the beginning of a loop
  * to generate the policy chain for the loop. A new [[CyclicPolicy]] is generated when the builder is terminated.
  *
  */
class PolicyBuilderInner(val policyFactories: List[(Policy, StatisticDataProvider) => Policy]) {
  /**
    * Adds a policy that emits given result once to the end of the builder.
    * @param result [[PolicyResult]] to be emitted.
    * @return New [[PolicyBuilderInner]]
    */
  def addOncePolicy(result: PolicyResult): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      new DoOncePolicy(result, policy)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  /**
    * Adds a policy that emits given result and loops forever to the end of the builder.
    * @param result [[PolicyResult]] to be emitted.
    * @return New [[PolicyBuilderInner]]
    */
  def addForeverPolicy(result: PolicyResult): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      new AlwaysDoPolicy(result)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  /**
    * Adds a policy that emits given result until a condition is met to the end of the builder.
    * @param result [[PolicyResult]] to be emitted.
    * @return New [[PolicyBuilderInner]]
    */
  def addRepeatPolicy(result: PolicyResult, condition: Condition): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      val generated = condition.generate(statistics)
      new RepeatUntilCondition(result, generated, policy)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  /**
    * Adds a transition to an external policy to the end of the builder.
    * @param externalPolicy [[Policy]] to be transitioned to.
    * @return New [[PolicyBuilderInner]]
    */
  def addPolicy(externalPolicy: Policy): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => externalPolicy
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  /**
    * Adds a conditional transition to an external policy to the end of the builder.
    * @param conditionalPolicy [[Policy]] to be transitioned to.
    * @param condition [[Condition]] to guard the transition.
    * @return New [[PolicyBuilderInner]]
    */
  def addConditionalPolicy(conditionalPolicy: Policy, condition: Condition): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      val generated = condition.generate(statistics)
      new IfPolicy(generated, conditionalPolicy, policy)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  /**
    * Generates a new cyclic policy using the loop described by this policy builder.
    * @return The generated and ready to use [[Policy]]
    */
  def generateCyclic(): Policy = new CyclicPolicy(policyFactories.reverse)
}
