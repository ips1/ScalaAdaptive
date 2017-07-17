package scalaadaptive.core.functions.identifiers

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * A type wrapping a function and two identifiers. The [[ClosureIdentifier]] is always present, and, in addition, one
  * special identifier (either [[MethodNameIdentifier]] or [[CustomIdentifier]]) can also be included.
  *
  * If not specified otherwise using the useClosures setting, the special identifier, if present, will always be
  * preferred.
  *
  * @param fun The function with tupled arguments.
  * @param closureIdentifier The [[ClosureIdentifier]] of the function (original, not the tupled).
  * @param specialIdentifier The special identifier of the function.
  * @param useClosures A setting that determines if the closure identifier should be used rather than the special
  *                    identifier.
  *
  */
class IdentifiedFunction[TArgType, TReturnType](val fun: (TArgType) => TReturnType,
                                                val closureIdentifier: ClosureIdentifier,
                                                val specialIdentifier: FunctionIdentifier,
                                                val useClosures: Boolean) {
  def this(fun: (TArgType) => TReturnType, closureIdentifier: ClosureIdentifier) =
    this(fun, closureIdentifier, closureIdentifier, false)

  def this(fun: (TArgType) => TReturnType, closureIdentifier: ClosureIdentifier, customIdentifier: FunctionIdentifier) =
    this(fun, closureIdentifier, customIdentifier, false)

  /** Returns the special identifier if its defined and if the useClosures isn't true. Returns closure identifier otherwise. */
  def identifier: FunctionIdentifier = if (useClosures) closureIdentifier else specialIdentifier

  /** Updates the useClosures setting. */
  def changeUseClosures(newUseClosures: Boolean): IdentifiedFunction[TArgType, TReturnType] =
    new IdentifiedFunction[TArgType, TReturnType](fun, closureIdentifier, specialIdentifier, newUseClosures)
}