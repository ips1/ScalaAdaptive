package scalaadaptive.core.functions.policies.builder

import scalaadaptive.core.functions.policies.PolicyResult
import scalaadaptive.core.functions.policies.PolicyResult.PolicyResult
import scalaadaptive.core.functions.policies.builder.conditions.{Condition, SimpleCondition}
import scalaadaptive.core.functions.statistics.StatisticDataProvider

/**
  * Created by pk250187 on 6/24/17.
  */
object BuilderImplicits {
  def produce(result: PolicyResult): PolicyBuilderNeedLimit =
    new PolicyBuilderNeedLimit(new PolicyBuilderInner(List()), result)

  implicit def createSimpleCondition(cond: (StatisticDataProvider) => Boolean): Condition = new SimpleCondition(cond)

  implicit def createIndependentCondition(cond: () => Boolean): Condition = new SimpleCondition((s) => cond())

  implicit def createNumericCondition[T: Numeric](extractor: (StatisticDataProvider) => T): GrowsByBuilderNeedAmount[T] =
    new GrowsByBuilderNeedAmount[T](extractor)

  implicit def createNumericConditionWithIndependentExtractor[T: Numeric](extractor: () => T): GrowsByBuilderNeedAmount[T] =
    new GrowsByBuilderNeedAmount[T]((s) => extractor())

  implicit def createSimpleBuilder(result: PolicyResult): PolicyBuilderNeedLimit = produce(result)

  val selectNew = PolicyResult.SelectNew
  val gatherData = PolicyResult.GatherData
  val useMost = PolicyResult.UseMost
  val useLast = PolicyResult.UseLast
}
