package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.builder.conditions.Condition
import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/24/17.
  */
class GrowsByCondition[T: Numeric](extractor: (StatisticDataProvider) => T, amount: T) extends Condition {
  override def generate(currentStatistics: StatisticDataProvider): (StatisticDataProvider) => Boolean = {
    val currentState = extractor(currentStatistics)
    (newStatistics) => {
      val newState = extractor(newStatistics)
      val diff = implicitly[Numeric[T]].minus(newState, currentState)
      implicitly[Numeric[T]].gt(diff, amount)
    }
  }
}
