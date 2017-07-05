package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{PolicyResult, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.{Condition, SimpleCondition}

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

  // Result shortcuts:
  val selectNew = PolicyResult.SelectNew
  val gatherData = PolicyResult.GatherData
  val useMost = PolicyResult.UseMost
  val useLast = PolicyResult.UseLast

  // Extractor predefs:
  val totalRunCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalRunCount
  val lastRunCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getLastRunCount
  val mostRunCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getMostRunCount
  val streakLength: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getStreakLength
  val totalTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalTime
  val totalFunctionTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalFunctionTime
  val totalOverheadTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalOverheadTime
  val totalGatherTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalGatherTime
  val totalGatherCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalGatherCount
}
