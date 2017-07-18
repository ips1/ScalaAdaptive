package scalaadaptive.api

/**
  * Created by Petr Kubat on 7/16/17.
  *
  * Package containing everything related to the concept of policies.
  *
  * The policies are a concept introduced to ScalaAdaptive in order to limit the overhead caused by selecting function
  * implementations to run. Policies allow skipping that process and using cached shortcuts.
  *
  * Policies technically represent a state machine that defines the behavior of the adaptive functions. A policy is an
  * instance of [[scalaadaptive.api.policies.Policy]]. Every adaptive function has a policy associated. Upon invocation,
  * the policy gets evaluated using [[scalaadaptive.api.policies.StatisticDataProvider]] which contains aggregated
  * information about the adaptive function history. The policy evaluation produces [[scalaadaptive.api.policies.PolicyResult.PolicyResult]]
  * and a new [[scalaadaptive.api.policies.Policy]] that replaces the old one, thus representing transition between
  * states.
  *
  * Policies can be chained together. There are some predefined implementations available (see [[scalaadaptive.api.policies.AlwaysSelectPolicy]],
  * [[scalaadaptive.api.policies.AlwaysUseLastPolicy]], [[scalaadaptive.api.policies.StartPolicy]], [[scalaadaptive.api.policies.LimitedGatherTimePolicy]],
  * [[scalaadaptive.api.policies.LimitedOverheadPolicy]] and others). A custom policies can be built using building
  * block policies (see [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilCondition]],
  * [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilGatherTimePolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilOverheadPolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilResetTimePolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilRunCountPolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.repeatuntil.RepeatUntilTotalTimePolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.AlwaysDoPolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.DoOncePolicy]],
  * [[scalaadaptive.api.policies.utilpolicies.IfPolicy]]).
  *
  * The most preferred way of creating custom policies is, however, the policy builder concept (see
  * [[scalaadaptive.api.policies.builder.PolicyBuilder]]). Custom policies can be built using a simple DSL.
  *
  * === Usage example ===
  *
  * {{{
  * val policy = (
  *   gatherData until (totalRunCount growsBy 50)
  *   andThen selectNew until (totalRunCount growsBy 100)
  *   andThenIf ((stats: StatisticDataProvider) => stats.getStreakLength >= 20) goTo useLastForever
  *   andThenRepeat
  * )
  *
  * val function = implementation1 _ or implementation2 withPolicy policy
  * }}}
  *
  */
package object policies {

}
