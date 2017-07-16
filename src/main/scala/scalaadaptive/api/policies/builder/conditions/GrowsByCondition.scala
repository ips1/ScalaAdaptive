package scalaadaptive.api.policies.builder.conditions

import scalaadaptive.api.policies.StatisticDataProvider

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * A complex condition that extract given value from statistics upon generation (at the start of the loop) and during
  * each evaluation within the loop extracts the value again. The condition is fulfilled when the difference of
  * the newly extracted value and the start of the loop value is bigger than the amount
  *
  * @param extractor The value extractor
  * @param amount The amount by which the extracted value has to grow.
  *
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
