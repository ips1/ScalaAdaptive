package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.Condition
import scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilCondition
import scalaadaptive.api.policies.utilpolicies.{AlwaysDoPolicy, CyclicPolicy, DoOncePolicy, IfPolicy}

/**
  * Created by Petr Kubat on 6/24/17.
  */
class PolicyBuilderInner(val policyFactories: List[(Policy, StatisticDataProvider) => Policy]) {
  def addOncePolicy(result: PolicyResult): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      new DoOncePolicy(result, policy)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  def addForeverPolicy(result: PolicyResult): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      new AlwaysDoPolicy(result)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  def addRepeatPolicy(result: PolicyResult, condition: Condition): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      val generated = condition.generate(statistics)
      new RepeatUntilCondition(result, generated, policy)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  def addPolicy(policy: Policy): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => policy
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  def addConditionalPolicy(conditionalPolicy: Policy, condition: Condition): PolicyBuilderInner = {
    val newFactory = (policy: Policy, statistics: StatisticDataProvider) => {
      val generated = condition.generate(statistics)
      new IfPolicy(generated, conditionalPolicy, policy)
    }
    new PolicyBuilderInner(newFactory :: policyFactories)
  }

  def generateCyclic(): Policy = new CyclicPolicy(policyFactories.reverse)
}
