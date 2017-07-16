package scalaadaptive.core.runtime.selection

import java.time.Duration

import scalaadaptive.api.grouping.Group
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.core.functions.RunResult
import scalaadaptive.core.functions.identifiers.{FunctionIdentifier, IdentifiedFunction}
import scalaadaptive.core.runtime.invocationtokens.InvocationTokenWithCallbacks

/**
  * Created by Petr Kubat on 3/26/17.
  *
  * Used to run one of multiple options passed in based on a history of previous runs of the options.
  *
  */
trait AdaptiveSelector {
  def selectAndRun[TArgType, TReturnType](options: Seq[IdentifiedFunction[TArgType, TReturnType]],
                                          arguments: TArgType,
                                          groupId: Group,
                                          inputDescriptor: Option[Long],
                                          limitedBy: Option[Duration],
                                          selection: Selection): RunResult[TReturnType]

  def selectAndRunWithDelayedMeasure[TArgType, TReturnType](options: Seq[IdentifiedFunction[TArgType, TReturnType]],
                                                            arguments: TArgType,
                                                            groupId: Group,
                                                            inputDescriptor: Option[Long],
                                                            limitedBy: Option[Duration],
                                                            selection: Selection): (TReturnType, InvocationTokenWithCallbacks)

  def gatherData[TArgType, TReturnType](options: Seq[IdentifiedFunction[TArgType, TReturnType]],
                                        arguments: TArgType,
                                        groupId: Group,
                                        inputDescriptor: Option[Long]): RunResult[TReturnType]

  def gatherDataWithDelayedMeasure[TArgType, TReturnType](options: Seq[IdentifiedFunction[TArgType, TReturnType]],
                                                          arguments: TArgType,
                                                          groupId: Group,
                                                          inputDescriptor: Option[Long]): (TReturnType, InvocationTokenWithCallbacks)

  def flushHistory(function: FunctionIdentifier)
}