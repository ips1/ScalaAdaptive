package scalaadaptive.core.functions.statistics

import scalaadaptive.core.functions.identifiers.IdentifiedFunction

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * Provider of the statistically important function implementations. The functions are returned directly, not using
  * any identifier - the intended use is with policies in the
  * [[scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker]].
  *
  */
trait StatisticFunctionProvider[TArgType, TRetType] {
  /** Retrieves the last selected function */
  def getLast: IdentifiedFunction[TArgType, TRetType]
  /** Retrieves the most selected function */
  def getMostSelectedFunction: IdentifiedFunction[TArgType, TRetType]
  /** Retrieves the least selected function */
  def getLeastSelectedFunction: IdentifiedFunction[TArgType, TRetType]
}
