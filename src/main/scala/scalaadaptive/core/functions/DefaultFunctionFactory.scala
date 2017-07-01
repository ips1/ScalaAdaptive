package scalaadaptive.core.functions

import scalaadaptive.api.grouping.NoGroup
import scalaadaptive.core.functions.references.ReferencedFunction
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by pk250187 on 5/27/17.
  */
class DefaultFunctionFactory extends FunctionFactory {
  override def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] = {
    val newConfig = AdaptiveInternal.getMultiFunctionDefaults
    new CombinedFunction[TArgType, TRetType](List(firstOption.changeUseClosures(newConfig.closureReferences)),
      None,
      (_) => NoGroup(),
      AdaptiveInternal.createLocalSelector(newConfig),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )
  }
}
