package scalaadaptive.api.policies.builder.conditions

import scalaadaptive.api.policies.StatisticDataProvider

/**
  * Created by pk250187 on 6/24/17.
  */
class AndCondition(condition1: Condition, condition2: Condition) extends Condition {
  override def generate(currentStatistics: StatisticDataProvider): (StatisticDataProvider) => Boolean = {
    val generated1 = condition1.generate(currentStatistics)
    val generated2 = condition2.generate(currentStatistics)
    (statistics) => generated1(statistics) && generated2(statistics)
  }
}
