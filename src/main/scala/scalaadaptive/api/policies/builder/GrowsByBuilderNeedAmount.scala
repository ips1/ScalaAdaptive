package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.StatisticDataProvider
import scalaadaptive.api.policies.builder.conditions.GrowsByCondition

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * A specific modification of [[PolicyBuilder]] inside a grows by condition - extractor has already been specified
  * and now the amount needs to be added.
  *
  */
class GrowsByBuilderNeedAmount[T: Numeric](val extractor: (StatisticDataProvider) => T) {
  def growsBy(amount: T) = new GrowsByCondition[T](extractor, amount)
}
