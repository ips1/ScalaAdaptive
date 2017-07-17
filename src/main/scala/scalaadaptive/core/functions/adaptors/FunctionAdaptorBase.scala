package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.analytics.AnalyticsData
import scalaadaptive.api.functions.InvocationToken
import scalaadaptive.api.grouping.Group
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.api.policies.Policy
import scalaadaptive.core.functions.{CombinedFunction, FunctionFactory}
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A base class for the function adaptor types (see [[scalaadaptive.core.functions.adaptors.FunctionAdaptor0]] and
  * similar types). Provides implementations of methods that are independent on the actual number of arguments and
  * therefore don't have to be replicated.
  *
  * Requires the extending adaptors to provide the [[scalaadaptive.core.functions.CombinedFunction]] that is wrapped
  * inside and a factory method that rewraps a new [[scalaadaptive.core.functions.CombinedFunction]] in the same type
  * and returns it (used for modification that create new instances).
  *
  * Note that all the methods provided are implementations of methods from the
  * [[scalaadaptive.api.functions.AdaptiveFunctionCommon]] trait, even though this type does not actually extend it.
  * We suppose that the non-abstract extending subtypes will extend the trait as well, thus providing implementations
  * in advance (Scala allows that during the object composition).
  *
  */
abstract class FunctionAdaptorBase[TArgType, TRetType, TFunctionAdaptorType] {
  /** The [[scalaadaptive.core.functions.CombinedFunction]] wrapped inside */
  protected val function: CombinedFunction[TArgType, TRetType]
  /** The factory method for new instances of the concrete type */
  protected val createNew: (CombinedFunction[TArgType, TRetType]) => TFunctionAdaptorType

  protected def functionFactory: FunctionFactory = AdaptiveInternal.getFunctionFactory
  private def invoker: CombinedFunctionInvoker = AdaptiveInternal.getFunctionInvoker

  def byTupled(newSelector: (TArgType) => Long): TFunctionAdaptorType =
    createNew(function.updateDescriptorFunction(Some(newSelector)))
  def groupByTupled(newSelector: (TArgType) => Group): TFunctionAdaptorType =
    createNew(function.updateGroupSelector(newSelector))

  def selectUsing(newSelection: Selection): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.selectUsing(newSelection)))
  def storeUsing(newStorage: Storage): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.storeUsing(newStorage)))
  def limitedTo(newDuration: Duration): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.limitedTo(newDuration)))
  def withPolicy(newPolicy: Policy): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.withPolicy(newPolicy)))
  def asClosures(closureIdentification: Boolean): TFunctionAdaptorType =
    createNew(function.updateFunctionConfig(function.functionConfig.asClosures(closureIdentification)))

  def invoke(args: TArgType): TRetType = invoker.invoke(function, args)
  def invokeWithDelayedMeasure(args: TArgType): (TRetType, InvocationToken) =
    invoker.invokeWithDelayedMeasure(function, args)

  def train(data: Seq[TArgType]): Unit = invoker.train(function, data)

  def toDebugString: String =
    function.functionIdentifiers.map(r => r.toString).mkString(", ")

  def flushHistory(): Unit = invoker.flushHistory(function)

  def setPolicy(policy: Policy): Unit = function.setPolicy(policy)

  def resetPolicy(): Unit = function.resetPolicy()

  def getAnalyticsData: Option[AnalyticsData] = function.getAnalyticsData
}
