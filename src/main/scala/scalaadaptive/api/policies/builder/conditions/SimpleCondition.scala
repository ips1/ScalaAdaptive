package scalaadaptive.api.policies.builder.conditions

import scalaadaptive.api.policies.StatisticDataProvider

/**
  * Created by Petr Kubat on 6/24/17.
  */
class SimpleCondition(cond: (StatisticDataProvider) => Boolean) extends Condition {
  override def generate(currentStatistics: StatisticDataProvider): (StatisticDataProvider) => Boolean = cond
}
