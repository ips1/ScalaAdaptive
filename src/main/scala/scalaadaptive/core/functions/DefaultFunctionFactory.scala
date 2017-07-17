package scalaadaptive.core.functions

import scalaadaptive.api.grouping.NoGroup
import scalaadaptive.core.functions.identifiers.IdentifiedFunction
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A default implementation of the [[scalaadaptive.core.functions.FunctionFactory]]. Creates
  * [[scalaadaptive.core.functions.CombinedFunction]] instances directy.
  *
  * The input descriptor is unspecified, the group selector selects [[scalaadaptive.api.grouping.NoGroup]], the config
  * is taken from the [[scalaadaptive.core.runtime.AdaptiveInternal]] (should be specified by
  * [[scalaadaptive.core.configuration.Configuration]]).
  *
  */
class DefaultFunctionFactory extends FunctionFactory {
  override def createFunction[TArgType, TRetType](firstOption: IdentifiedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType] = {
    val newConfig = AdaptiveInternal.getAdaptiveFunctionDefaults
    new CombinedFunction[TArgType, TRetType](List(firstOption.changeUseClosures(newConfig.closureIdentifier)),
      None,
      (_) => NoGroup(),
      AdaptiveInternal.createLocalSelector(newConfig),
      AdaptiveInternal.createAnalytics(),
      newConfig
    )
  }
}
