package scalaadaptive.core.functions

import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.functions.adaptors.{FunctionConfig, StorageBasedRunner}
import scalaadaptive.core.functions.references.ReferencedFunction

/**
  * Created by pk250187 on 5/27/17.
  */
class DefaultFunctionFactory extends FunctionFactory {
  private def updateFunctionsWithConfig[TArgType, TRetType](functions: Seq[ReferencedFunction[TArgType, TRetType]],
                                                            config: FunctionConfig) = {
    functions.map(f => new ReferencedFunction[TArgType, TRetType](f.fun,
      f.closureReference,
      f.customReference,
      config.closureReferences))
  }

  override def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](updateFunctionsWithConfig(List(firstOption), AdaptiveInternal.getMultiFunctionDefaults),
      None,
      new StorageBasedRunner(AdaptiveInternal.getMultiFunctionDefaults.storage),
      AdaptiveInternal.createAnalytics(),
      AdaptiveInternal.getMultiFunctionDefaults
    )

  override def changeFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  inputDescriptorSelector: Option[(TArgType) => Long]): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](function.functions,
      inputDescriptorSelector,
      new StorageBasedRunner(function.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      function.functionConfig
    )

  override def changeFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  newConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](updateFunctionsWithConfig(function.functions, newConfig),
      function.inputDescriptorSelector,
      new StorageBasedRunner(newConfig.storage),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )

  override def mergeFunctions[TArgType, TRetType](firstFunction: MultipleImplementationFunction[TArgType, TRetType],
                                                  secondFunction: MultipleImplementationFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType] = {
    new MultipleImplementationFunction[TArgType, TRetType](firstFunction.functions ++ secondFunction.functions,
      firstFunction.inputDescriptorSelector,
      new StorageBasedRunner(firstFunction.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      firstFunction.functionConfig
    )
  }
}
