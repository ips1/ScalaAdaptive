package scalaadaptive.core.functions

import scala.collection.mutable
import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.adaptors.InvocationToken
import scalaadaptive.api.grouping.{Group, GroupId, NoGroup}
import scalaadaptive.api.options.Storage
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime.invocationtokens.SimpleInvocationToken
import scalaadaptive.api.policies.{Policy, PolicyResult}
import scalaadaptive.core.functions.statistics.FunctionStatistics
import scalaadaptive.core.functions.adaptors.{FunctionConfig, StorageBasedSelector}
import scalaadaptive.core.functions.analytics.AnalyticsCollector
import scalaadaptive.core.runtime.{AdaptiveInternal, AdaptiveSelector}

/**
  * Created by pk250187 on 5/21/17.
  */
class CombinedFunction[TArgType, TRetType](val functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                           val inputDescriptorSelector: Option[(TArgType) => Long],
                                           val groupSelector: (TArgType) => GroupId,
                                           val localSelector: Option[AdaptiveSelector],
                                           val analytics: AnalyticsCollector,
                                           val functionConfig: FunctionConfig) {
  private def createDefaultPolicy() = functionConfig.startPolicy
  private def createDefaultStatistics() = new FunctionStatistics[TArgType, TRetType](functions.head,
    ref => functions.find(f => f.reference == ref))
  private def createDefaultFunctionData() = new FunctionData(createDefaultStatistics(), createDefaultPolicy())

  private val ungroupedData = createDefaultFunctionData()
  private val groupedData = new mutable.HashMap[Long, FunctionData[TArgType, TRetType]]

  val functionReferences: Seq[FunctionReference] =
    functions.map(f => if (functionConfig.closureReferences) f.closureReference else f.reference)

  def getData(groupId: GroupId): FunctionData[TArgType, TRetType] = groupId match {
    case NoGroup() => ungroupedData
    case Group(id) => groupedData.getOrElseUpdate(id, createDefaultFunctionData())
  }

  def generateInputDescriptor(arguments: TArgType): Option[Long] =
    inputDescriptorSelector.map(sel => sel(arguments))

  def setPolicy(policy: Policy): Unit = {
    ungroupedData.currentPolicy = policy
    groupedData.foreach(_._2.currentPolicy = policy)
  }

  def resetPolicy(): Unit = setPolicy(createDefaultPolicy())

  def getAnalyticsData: Option[AnalyticsData] = analytics.getAnalyticsData

  private def updateReferencedFunctionsWithConfig(functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                  config: FunctionConfig) = {
    functions.map(_.changeUseClosures(config.closureReferences))
  }

  def updateInputDescriptor(inputDescriptorSelector: Option[(TArgType) => Long]): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](functions,
      inputDescriptorSelector,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )

  def updateGroupSelector(groupSelector: (TArgType) => GroupId): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](functions,
      inputDescriptorSelector,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )


  def updateFunctionConfig(newConfig: FunctionConfig): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](updateReferencedFunctionsWithConfig(functions, newConfig),
      inputDescriptorSelector,
      groupSelector,
      AdaptiveInternal.createLocalSelector(newConfig),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )

  def mergeFunctions(secondFunction: CombinedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] = {
    new CombinedFunction[TArgType, TRetType](functions ++ secondFunction.functions,
      inputDescriptorSelector,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )
  }
}
