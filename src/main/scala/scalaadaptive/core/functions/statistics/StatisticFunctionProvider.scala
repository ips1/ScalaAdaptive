package scalaadaptive.core.functions.statistics

import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}

/**
  * Created by pk250187 on 5/21/17.
  */
trait StatisticFunctionProvider[TArgType, TRetType] {
  def getLast: ReferencedFunction[TArgType, TRetType]
  def getMostSelectedFunction: ReferencedFunction[TArgType, TRetType]
  def getLeastSelectedFunction: ReferencedFunction[TArgType, TRetType]
}
