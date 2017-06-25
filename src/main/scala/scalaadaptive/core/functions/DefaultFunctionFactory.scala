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

  override def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](updateFunctionsWithConfig(List(firstOption), AdaptiveInternal.getMultiFunctionDefaults),
      None,
      (_) => NoGroup(),
      new StorageBasedSelector(AdaptiveInternal.getMultiFunctionDefaults.storage),
      AdaptiveInternal.createAnalytics(),
      AdaptiveInternal.getMultiFunctionDefaults
    )

  override def changeFunction[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                  inputDescriptorSelector: Option[(TArgType) => Long]): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](function.functions,
      inputDescriptorSelector,
      function.groupSelector,
      new StorageBasedSelector(function.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      function.functionConfig
    )

  override def changeFunction[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                  groupSelector: (TArgType) => GroupId): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](function.functions,
      function.inputDescriptorSelector,
      groupSelector,
      new StorageBasedSelector(function.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      function.functionConfig
    )


  override def changeFunction[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                                  newConfig: FunctionConfig): CombinedFunction[TArgType, TRetType] =
    new CombinedFunction[TArgType, TRetType](updateFunctionsWithConfig(function.functions, newConfig),
      function.inputDescriptorSelector,
      function.groupSelector,
      new StorageBasedSelector(newConfig.storage),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )

  override def mergeFunctions[TArgType, TRetType](firstFunction: CombinedFunction[TArgType, TRetType],
                                                  secondFunction: CombinedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] = {
    new CombinedFunction[TArgType, TRetType](firstFunction.functions ++ secondFunction.functions,
      firstFunction.inputDescriptorSelector,
      firstFunction.groupSelector,
      new StorageBasedSelector(firstFunction.functionConfig.storage),
      AdaptiveInternal.createAnalytics(),
      firstFunction.functionConfig
    )
  }
}
