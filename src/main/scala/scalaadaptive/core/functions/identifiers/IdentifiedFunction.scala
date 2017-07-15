package scalaadaptive.core.functions.identifiers

/**
  * Created by pk250187 on 5/21/17.
  */
class IdentifiedFunction[TArgType, TReturnType](val fun: (TArgType) => TReturnType,
                                                val closureIdentifier: ClosureIdentifier,
                                                val customIdentifier: FunctionIdentifier,
                                                val useClosures: Boolean) {
  def this(fun: (TArgType) => TReturnType, closureIdentifier: ClosureIdentifier) =
    this(fun, closureIdentifier, closureIdentifier, false)

  def this(fun: (TArgType) => TReturnType, closureIdentifier: ClosureIdentifier, customIdentifier: FunctionIdentifier) =
    this(fun, closureIdentifier, customIdentifier, false)

  def identifier: FunctionIdentifier = if (useClosures) closureIdentifier else customIdentifier

  def changeUseClosures(newUseClosures: Boolean): IdentifiedFunction[TArgType, TReturnType] =
    new IdentifiedFunction[TArgType, TReturnType](fun, closureIdentifier, customIdentifier, newUseClosures)
}