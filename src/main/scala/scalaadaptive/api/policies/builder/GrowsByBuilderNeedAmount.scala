package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.StatisticDataProvider

/**
  * Created by Petr Kubat on 6/24/17.
  */
class GrowsByBuilderNeedAmount[T: Numeric](val extractor: (StatisticDataProvider) => T) {
  def growsBy(amount: T) = new GrowsByCondition[T](extractor, amount)
}
