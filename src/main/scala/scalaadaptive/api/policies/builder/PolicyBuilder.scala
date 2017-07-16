package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{Policy, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.{Condition, SimpleCondition}

/**
  * A representation of a policy builder forming a not-yet-terminated policy loop, but with already terminated result
  * stage. Waining for either loop termination or next result at this moment.
  *
  * Whenever the loop is terminated, a new [[scalaadaptive.api.policies.utilpolicies.CyclicPolicy]] is generated.
  *
  * For more information about the internal representation of the incomplete policy chain inside the builder, see
  * [[PolicyBuilderInner]].
  *
  * @param inner The inner builder containing the parts of the loop built so far.
  */
class PolicyBuilder(val inner: PolicyBuilderInner) {
  /**
    * Introduces additional result at the end of currently built sequence
    */
  def andThen(result: PolicyResult): PolicyBuilderNeedLimit =
    new PolicyBuilderNeedLimit(inner, result)

  /**
    * Terminates the policy loop by transitioning to a [[Policy]]. Ends the builder phase and generates a [[Policy]]
    * @return The built [[Policy]]
    */
  def andThenGoTo(externalPolicy: Policy): Policy =
    inner.addPolicy(externalPolicy).generateCyclic()

  /**
    * Terminates the policy loop by returning back to the beginning. Ends the builder phase and generates a [[Policy]]
    * @return The built [[Policy]]
    */
  def andThenRepeat: Policy =
    inner.generateCyclic()

  /**
    * Adds a conditional transition to a [[Policy]] to the end of the policy loop. Dooes not terminate the builder
    * phase, as the condition can get evaluated to false and current policy will continue.
    * @param condition The condition for the transition.
    */
  def andThenIf(condition: Condition): PolicyBuilderNeedNextPolicy =
    new PolicyBuilderNeedNextPolicy(inner, condition)

  /**
    * Adds a conditional transition to a [[Policy]] to the end of the policy loop. Dooes not terminate the builder
    * phase, as the condition can get evaluated to false and current policy will continue.
    * @param condition The condition for the transition.
    */
  def andThenIf(condition: (StatisticDataProvider) => Boolean): PolicyBuilderNeedNextPolicy =
    andThenIf(new SimpleCondition(condition))
}
