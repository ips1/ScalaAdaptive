package scalaadaptive.core.adaptors

import java.time.Duration

import scalaadaptive.api.adaptors.MultiFunctionCommon
import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.policies.Policy
import scalaadaptive.core.runtime.{FunctionFactory, MultipleImplementationFunction, ReferencedFunction}

/**
  * Created by pk250187 on 5/27/17.
  */
abstract class FunctionAdaptorBase[TArgType, TRetType, TFunctionAdaptorType] {
  protected val function: MultipleImplementationFunction[TArgType, TRetType]
  protected val functionFactory: FunctionFactory
  protected val createNew: (MultipleImplementationFunction[TArgType, TRetType]) => TFunctionAdaptorType

  private def updateFunctionsWithClosureIdentification(asClosures: Boolean) =
    function.functions.map(f =>
      new ReferencedFunction[TArgType, TRetType](f.fun, f.closureReference, f.customReference, asClosures))

  def byGrouped(newSelector: (TArgType) => Long): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function.functions, Some(newSelector), function.adaptorConfig))

  def selectUsing(newSelection: Selection): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.adaptorConfig.selectUsing(newSelection)))
  def storeUsing(newStorage: Storage): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.adaptorConfig.storeUsing(newStorage)))
  def limitedTo(newDuration: Duration): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.adaptorConfig.limitedTo(newDuration)))
  def withPolicy(newPolicy: Policy): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](function, function.adaptorConfig.withPolicy(newPolicy)))

  def asClosures(closureIdentification: Boolean): TFunctionAdaptorType =
    createNew(functionFactory.createFunction[TArgType, TRetType](
      updateFunctionsWithClosureIdentification(closureIdentification), function.inputDescriptorSelector,
      function.adaptorConfig.asClosures(closureIdentification)))

  def train(data: Seq[TArgType]): Unit = function.train(data)

  def toDebugString: String =
    function.functionReferences.map(r => r.toString).mkString(", ")

  def flushHistory(): Unit = function.flushHistory()

  def setPolicy(policy: Policy): Unit = function.setPolicy(policy)

  def resetPolicy(): Unit = function.resetPolicy()
}
