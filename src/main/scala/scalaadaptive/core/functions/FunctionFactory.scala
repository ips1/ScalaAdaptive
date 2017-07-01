package scalaadaptive.core.functions

import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.references.ReferencedFunction

/**
  * Created by pk250187 on 5/27/17.
  */
trait FunctionFactory {
  def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType]
}
