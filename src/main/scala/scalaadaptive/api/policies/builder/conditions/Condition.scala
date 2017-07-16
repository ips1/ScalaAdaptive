package scalaadaptive.api.policies.builder.conditions

import scalaadaptive.api.policies.StatisticDataProvider

/**
  * Created by Petr Kubat on 6/23/17.
  *
  * A condition representation in the policy builder.
  *
  */

trait Condition {
  /**
    * Creates the actual condition evaluating function based on the start-of-the-loop statistics.
    * Is called each time upon enering the main policy loop.
    * @param currentStatistics Statistics at the time of entering the main policy loop.
    * @return The condition that has to be fulfilled for this round of the loop.
    */
  def generate(currentStatistics: StatisticDataProvider): (StatisticDataProvider) => Boolean

  /**
    * Combines this condition with another condition using the and operator.
    * @param condition The other condition.
    * @return The combined condition (both have to be fulfilled).
    */
  def and(condition: Condition) = new AndCondition(this, condition)
}
