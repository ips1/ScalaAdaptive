package scalaadaptive.core.functions

import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.identifiers.IdentifiedFunction

/**
  * Created by Petr Kubat on 5/27/17.
  */
trait FunctionFactory {
  def createFunction[TArgType, TRetType](firstOption: IdentifiedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType]
}
