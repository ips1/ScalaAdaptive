package scalaadaptive.core.runtime.statistics

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.ReferencedFunction

/**
  * Created by pk250187 on 5/21/17.
  */
trait StatisticFunctionProvider[TArgType, TRetType] {
  def getLast: ReferencedFunction[TArgType, TRetType]
  def getMostSelectedFunction: ReferencedFunction[TArgType, TRetType]
  def getLeastSelectedFunction: ReferencedFunction[TArgType, TRetType]
}
