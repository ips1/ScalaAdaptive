package scalaadaptive.core.functions.statistics

import scalaadaptive.core.functions.identifiers.{FunctionIdentifier, IdentifiedFunction}

/**
  * Created by pk250187 on 5/21/17.
  */
trait StatisticFunctionProvider[TArgType, TRetType] {
  def getLast: IdentifiedFunction[TArgType, TRetType]
  def getMostSelectedFunction: IdentifiedFunction[TArgType, TRetType]
  def getLeastSelectedFunction: IdentifiedFunction[TArgType, TRetType]
}
