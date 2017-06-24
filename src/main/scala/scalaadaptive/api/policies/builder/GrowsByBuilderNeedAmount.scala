package scalaadaptive.api.policies.builder

import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/24/17.
  */
class GrowsByBuilderNeedAmount[T: Numeric](val extractor: (StatisticDataProvider) => T) {
  def growsBy(amount: T) = new GrowsByCondition[T](extractor, amount)
}
