package scalaadaptive.core.functions

import scalaadaptive.core.runtime.AdaptiveInternal
import scalaadaptive.core.functions.adaptors.{CustomRunner, FunctionConfig}

/**
  * Created by pk250187 on 5/27/17.
  */
class DefaultFunctionFactory extends FunctionFactory {
  override def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](List(firstOption),
      None,
      new CustomRunner(AdaptiveInternal.getMultiFunctionDefaults.storage),
      AdaptiveInternal.getMultiFunctionDefaults
    )

  // TODO: Empty sequence?
  override def createFunction[TArgType, TRetType](options: Seq[ReferencedFunction[TArgType, TRetType]],
                                                  inputDescriptorSelector: Option[(TArgType) => Long],
                                                  adaptorConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](options,
      inputDescriptorSelector,
      new CustomRunner(adaptorConfig.storage),
      adaptorConfig
    )

  override def createFunction[TArgType, TRetType](function: MultipleImplementationFunction[TArgType, TRetType],
                                                  adaptorConfig: FunctionConfig): MultipleImplementationFunction[TArgType, TRetType] =
    new MultipleImplementationFunction[TArgType, TRetType](function.functions,
      function.inputDescriptorSelector,
      new CustomRunner(adaptorConfig.storage),
      adaptorConfig
    )

  override def mergeFunctions[TArgType, TRetType](firstFunction: MultipleImplementationFunction[TArgType, TRetType],
                                                  secondFunction: MultipleImplementationFunction[TArgType, TRetType]): MultipleImplementationFunction[TArgType, TRetType] = {
    new MultipleImplementationFunction[TArgType, TRetType](firstFunction.functions ++ secondFunction.functions,
      firstFunction.inputDescriptorSelector,
      new CustomRunner(firstFunction.functionConfig.storage),
      firstFunction.functionConfig
    )
  }
}
