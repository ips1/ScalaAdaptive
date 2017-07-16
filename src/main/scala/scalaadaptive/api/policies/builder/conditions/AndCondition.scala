package scalaadaptive.api.policies.builder.conditions

import scalaadaptive.api.policies.StatisticDataProvider

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * Implementation of a condition combined from two other condition using the and logical operator.
  *
  */
class AndCondition(condition1: Condition, condition2: Condition) extends Condition {
  override def generate(currentStatistics: StatisticDataProvider): (StatisticDataProvider) => Boolean = {
    val generated1 = condition1.generate(currentStatistics)
    val generated2 = condition2.generate(currentStatistics)
    (statistics) => generated1(statistics) && generated2(statistics)
  }
}
