package scalaadaptive.core.functions

import scalaadaptive.api.grouping.NoGroup
import scalaadaptive.core.functions.identifiers.IdentifiedFunction
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by Petr Kubat on 5/27/17.
  */
class DefaultFunctionFactory extends FunctionFactory {
  override def createFunction[TArgType, TRetType](firstOption: IdentifiedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] = {
    val newConfig = AdaptiveInternal.getMultiFunctionDefaults
    new CombinedFunction[TArgType, TRetType](List(firstOption.changeUseClosures(newConfig.closureIdentifier)),
      None,
      (_) => NoGroup(),
      AdaptiveInternal.createLocalSelector(newConfig),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )
  }
}
