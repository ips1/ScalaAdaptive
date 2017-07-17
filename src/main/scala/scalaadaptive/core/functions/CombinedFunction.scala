package scalaadaptive.core.functions

import scala.collection.mutable
import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.grouping.{Group, GroupId, NoGroup}
import scalaadaptive.api.options.Selection
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.policies.Policy
import scalaadaptive.core.functions.analytics.AnalyticsCollector
import scalaadaptive.core.functions.identifiers.{FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.functions.statistics.FunctionStatistics
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.runtime.selection.AdaptiveSelector

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * The main internal representation of a ScalaAdaptive function with multiple implementations (combined function).
  *
  * Contains primarily a sequence of functions with their identifiers assigned, and a
  * [[scalaadaptive.core.functions.FunctionConfig]] that sets up its behavior. These parameters are immutable
  * and a new instance has to be created in order to change them.
  *
  * In addition, contains a mutable state - the [[scalaadaptive.core.functions.FunctionData]], once for each group of
  * input the function encountered.
  *
  * Servers only as a data holder, does not have any significant functionality and cannot invoke itself.
  *
  * @param functions The implementations (along with their identifiers) included in the function.
  * @param descriptorFunction The function for extracting input descriptor from input.
  * @param groupSelector The function for selecting a group for an input.
  * @param localSelector A local instance of [[scalaadaptive.core.runtime.selection.AdaptiveSelector]] in case the
  *                      [[scalaadaptive.api.options.Storage]] is set to [[scalaadaptive.api.options.Storage.Local]].
  * @param analytics An [[scalaadaptive.core.functions.analytics.AnalyticsCollector]] that collects the analytics data
  *                  for the function.
  * @param functionConfig A [[scalaadaptive.core.functions.FunctionConfig]] of the function.
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

  /** Sequence of identifiers of the implementations of the function */
  val functionIdentifiers: Seq[FunctionIdentifier] =
    functions.map(f => if (functionConfig.closureIdentifier) f.closureIdentifier else f.identifier)

  /** Fetches [[scalaadaptive.core.functions.FunctionData]] for the specified group */
  def getData(groupId: Group): FunctionData[TArgType, TRetType] = groupId match {
    case NoGroup() => ungroupedData
    case GroupId(id) => groupedData.getOrElseUpdate(id, createDefaultFunctionData())
  }

  /** Generates input descriptor for an input */
  def getInputDescriptor(arguments: TArgType): Option[Long] =
    descriptorFunction.map(sel => sel(arguments))

  /** Selects a group for an input */
  def getGroup(arguments: TArgType): Group =
    groupSelector(arguments)

  /** Retrieves the [[scalaadaptive.api.options.Selection]] that should be used with the function (either from the
    * config, or as a default depending on the descriptor function being specified or not). */
  def getSelection: Selection = functionConfig.selection.getOrElse {
    if (descriptorFunction.isDefined) Selection.InputBased else Selection.MeanBased
  }

  /** Sets current policy for this function (throughout all the groups) */
  def setPolicy(policy: Policy): Unit = {
    ungroupedData.currentPolicy = policy
    groupedData.foreach(_._2.currentPolicy = policy)
  }

  /** Sets current policy for this function to the default policy (throughout all the groups) */
  def resetPolicy(): Unit = setPolicy(createDefaultPolicy())

  /** Retrieves the analytics data (if collected) */
  def getAnalyticsData: Option[AnalyticsData] = analytics.getAnalyticsData

  private def updateIdentifiedFunctionsWithConfig(functions: Seq[IdentifiedFunction[TArgType, TRetType]],
                                                  config: FunctionConfig) = {
    functions.map(_.changeUseClosures(config.closureIdentifier))
  }

  /** Creates a new [[scalaadaptive.core.functions.CombinedFunction]] with changed input descriptor selector function. */
  def updateDescriptorFunction(descriptorFunction: Option[(TArgType) => Long]): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](functions,
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )

  /** Creates a new [[scalaadaptive.core.functions.CombinedFunction]] with changed group selector function. */
  def updateGroupSelector(groupSelector: (TArgType) => Group): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](functions,
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(functionConfig),
      AdaptiveInternal.createAnalytics(),
      functionConfig
    )

  /** Creates a new [[scalaadaptive.core.functions.CombinedFunction]] with changed function config. */
  def updateFunctionConfig(newConfig: FunctionConfig): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](updateIdentifiedFunctionsWithConfig(functions, newConfig),
      descriptorFunction,
      groupSelector,
      AdaptiveInternal.createLocalSelector(newConfig),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )

  /**
    * Creates a new [[scalaadaptive.core.functions.CombinedFunction]] by merging it with a different
    * [[scalaadaptive.core.functions.CombinedFunction]]. Note that all the setup from this function survives, only the
    * implementations from the secondFunction are added.
    *
    * This is the main function that actually allows putting together multiple implementations.
    *
    * @param secondFunction The other [[scalaadaptive.core.functions.CombinedFunction]] whose implementations will be
    *                       added.
    * @return The merged function (a new [[scalaadaptive.core.functions.CombinedFunction]] instance).
    */
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
