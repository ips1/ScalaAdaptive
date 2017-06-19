package scalaadaptive.core.functions

import scalaadaptive.api.grouping.{GroupId, NoGroup}
import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.functions.adaptors.{FunctionConfig, StorageBasedSelector}
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
      (_) => NoGroup(),
      new StorageBasedSelector(AdaptiveInternal.getMultiFunctionDefaults.storage),
      AdaptiveInternal.createAnalytics(),
      AdaptiveInternal.getMultiFunctionDefaults
    )

  override def changeFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  inputDescriptorSelector: Option[(TArgType) => Long]): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](function.functions,
      inputDescriptorSelector,
      function.groupSelector,
      new StorageBasedSelector(function.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      function.functionConfig
    )

  override def changeFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  groupSelector: (TArgType) => GroupId): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](function.functions,
      function.inputDescriptorSelector,
      groupSelector,
      new StorageBasedSelector(function.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      function.functionConfig
    )


  override def changeFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  newConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](updateFunctionsWithConfig(function.functions, newConfig),
      function.inputDescriptorSelector,
      function.groupSelector,
      new StorageBasedSelector(newConfig.storage),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )

  override def mergeFunctions[TArgType, TRetType](firstFunction: MultipleImplementationFunction[TArgType, TRetType],
                                                  secondFunction: MultipleImplementationFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType] = {
    new MultipleImplementationFunction[TArgType, TRetType](firstFunction.functions ++ secondFunction.functions,
      firstFunction.inputDescriptorSelector,
      firstFunction.groupSelector,
      new StorageBasedSelector(firstFunction.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      firstFunction.functionConfig
    )
  }
}
