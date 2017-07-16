package scalaadaptive.api.policies.builder

import scalaadaptive.api.policies.{PolicyResult, StatisticDataProvider}
import scalaadaptive.api.policies.PolicyResult.PolicyResult
import scalaadaptive.api.policies.builder.conditions.{Condition, SimpleCondition}

/**
  * Created by Petr Kubat on 6/24/17.
  *
  * An object that provides implicit conversions related to the policy builder items.
  *
  */
object BuilderImplicits {
  /**
    * Creates a new [[PolicyBuilderNeedLimit]] with inner [[PolicyBuilder]] from a result used in the starting policy
    * of the loop.
    */
  def produce(result: PolicyResult): PolicyBuilderNeedLimit =
    new PolicyBuilderNeedLimit(new PolicyBuilderInner(List()), result)

  /**
    * Converts boolean-returning function that accepts [[StatisticDataProvider]] to a [[SimpleCondition]].
    */
  implicit def createSimpleCondition(cond: (StatisticDataProvider) => Boolean): Condition = new SimpleCondition(cond)

  /**
    * Converts boolean-returning function to a [[SimpleCondition]].
    */
  implicit def createIndependentCondition(cond: () => Boolean): Condition = new SimpleCondition((s) => cond())

  /**
    * Creates [[GrowsByBuilderNeedAmount]] from a simple value extractor accepting [[StatisticDataProvider]].
    */
  implicit def createNumericCondition[T: Numeric](extractor: (StatisticDataProvider) => T): GrowsByBuilderNeedAmount[T] =
    new GrowsByBuilderNeedAmount[T](extractor)

  /**
    * Creates [[GrowsByBuilderNeedAmount]] from a simple value extractor.
    */
  implicit def createNumericConditionWithIndependentExtractor[T: Numeric](extractor: () => T): GrowsByBuilderNeedAmount[T] =
    new GrowsByBuilderNeedAmount[T]((s) => extractor())

  /**
    * A shortcut to convert a [[PolicyResult]] to a builder producing the result. See [[produce]].
    */
  implicit def createSimpleBuilder(result: PolicyResult): PolicyBuilderNeedLimit = produce(result)

  // Result shortcuts:
  val selectNew = PolicyResult.SelectNew
  val gatherData = PolicyResult.GatherData
  val useMost = PolicyResult.UseMost
  val useLast = PolicyResult.UseLast

  // Extractor predefs:
  val totalRunCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalRunCount
  val lastRunCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getLastSelectCount
  val mostRunCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getMostSelectCount
  val streakLength: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getStreakLength
  val totalTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalTime
  val totalFunctionTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalFunctionTime
  val totalOverheadTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalOverheadTime
  val totalGatherTime: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalGatherTime
  val totalGatherCount: (StatisticDataProvider) => Long = (stats: StatisticDataProvider) => stats.getTotalGatherCount
}
