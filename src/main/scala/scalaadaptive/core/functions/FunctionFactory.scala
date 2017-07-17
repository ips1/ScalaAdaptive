package scalaadaptive.core.functions

import scalaadaptive.core.functions.identifiers.IdentifiedFunction

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A factory for creating [[CombinedFunction]] with only a single implementation.
  *
  */
trait FunctionFactory {
  /** Creates [[CombinedFunction]] using a single implementation [[IdentifiedFunction]]. */
  def createFunction[TArgType, TRetType](firstOption: IdentifiedFunction[TArgType, TRetType]): CombinedFunction[TArgType, TRetType]
}
