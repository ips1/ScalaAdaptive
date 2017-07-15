package scalaadaptive.core.functions

import scala.collection.mutable
import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.grouping.{Group, GroupId, NoGroup}
import scalaadaptive.api.options.Selection
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.policies.Policy
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.analytics.AnalyticsCollector
import scalaadaptive.core.functions.identifiers.{FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.functions.statistics.FunctionStatistics
import scalaadaptive.core.runtime.{AdaptiveInternal, AdaptiveSelector}

/**
  * Created by pk250187 on 5/21/17.
  */
class CombinedFunction[TArgType, TRetType](val functions: Seq[IdentifiedFunction[TArgType, TRetType]],
                                           private val descriptorFunction: Option[(TArgType) => Long],
                                           private val groupSelector: (TArgType) => Group,
                                           val localSelector: Option[AdaptiveSelector],
                                           val analytics: AnalyticsCollector,
                                           val functionConfig: FunctionConfig) {
  private def createDefaultPolicy() = functionConfig.startPolicy
  private def createDefaultStatistics() = new FunctionStatistics[TArgType, TRetType](functions.head,
    ref => functions.find(f => f.identifier == ref))
  private def createDefaultFunctionData() = new FunctionData(createDefaultStatistics(), createDefaultPolicy())

  private val ungroupedData = createDefaultFunctionData()
  private val groupedData = new mutable.HashMap[Long, FunctionData[TArgType, TRetType]]

  val functionIdentifiers: Seq[FunctionIdentifier] =
    functions.map(f => if (functionConfig.closureIdentifier) f.closureIdentifier else f.identifier)

  def getData(groupId: Group): FunctionData[TArgType, TRetType] = groupId match {
    case NoGroup() => ungroupedData
    case GroupId(id) => groupedData.getOrElseUpdate(id, createDefaultFunctionData())
  }

  def getInputDescriptor(arguments: TArgType): Option[Long] =
    descriptorFunction.map(sel => sel(arguments))

  def getGroup(arguments: TArgType): Group =
    groupSelector(arguments)

  def getSelection: Selection = functionConfig.selection.getOrElse {
    if (descriptorFunction.isDefined) Selection.InputBased else Selection.MeanBased
  }

  def setPolicy(policy: Policy): Unit = {
    ungroupedData.currentPolicy = policy
    groupedData.foreach(_._2.currentPolicy = policy)
  }

  def resetPolicy(): Unit = setPolicy(createDefaultPolicy())

  def getAnalyticsData: Option[AnalyticsData] = analytics.getAnalyticsData

  private def updateIdentifiedFunctionsWithConfig(functions: Seq[IdentifiedFunction[TArgType, TRetType]],
                                                  config: FunctionConfig) = {
    functions.map(_.changeUseClosures(config.closureIdentifier))
  }

  def updateDescriptorFunction(descriptorFunction: Option[(TArgType) => Long]): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](functions,
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )

  def updateGroupSelector(groupSelector: (TArgType) => Group): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](functions,
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )


  def updateFunctionConfig(newConfig: FunctionConfig): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](updateIdentifiedFunctionsWithConfig(functions, newConfig),
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(newConfig),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )

  def mergeFunctions(secondFunction: CombinedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] = {
    new CombinedFunction[TArgType, TRetType](functions ++ secondFunction.functions,
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )
  }
}
