package scalaadaptive.core.functions

import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.references.ReferencedFunction

/**
  * Created by pk250187 on 5/27/17.
  */
trait FunctionFactory {
  def createFunction[TArgType, TRetType](firstOption: ReferencedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType]

  def changeFunction[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                         inputDescriptorSelector: Option[(TArgType) => Long]): CombinedFunction[TArgType, TRetType]

  def changeFunction[TArgType, TRetType](function: CombinedFunction[TArgType, TRetType],
                                         groupSelector: (TArgType) => GroupId): CombinedFunction[TArgType, TRetType]

  def changeFunction[TArgType, TRetType](multipleImplementationFunction: CombinedFunction[TArgType, TRetType],
                                         adaptorConfig: FunctionConfig): CombinedFunction[TArgType, TRetType]

  def mergeFunctions[TArgType, TRetType](firstFunction: CombinedFunction[TArgType, TRetType],
                                         secondFunction: CombinedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType]
}
