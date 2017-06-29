package scalaadaptive.core.runtime

import java.time.Duration

import scalaadaptive.api.grouping.GroupId
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.core.performance.PerformanceTracker
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.invocationtokens.InvocationTokenWithCallbacks
import scalaadaptive.core.functions.RunResult

/**
  * Created by pk250187 on 3/26/17.
  *
  * Used to run one of multiple options passed in based on a history of previous runs of the options.
  *
  */
trait AdaptiveSelector {
  def selectAndRun[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                          arguments: TArgType,
                                          groupId: GroupId,
                                          inputDescriptor: Option[Long],
                                          limitedBy: Option[Duration],
                                          selection: Selection): RunResult[TReturnType]

  def selectAndRunWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                            arguments: TArgType,
                                                            groupId: GroupId,
                                                            inputDescriptor: Option[Long],
                                                            limitedBy: Option[Duration],
                                                            selection: Selection): (TReturnType, InvocationTokenWithCallbacks)

  def gatherData[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                        arguments: TArgType,
                                        groupId: GroupId,
                                        inputDescriptor: Option[Long]): RunResult[TReturnType]

  def gatherDataWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                          arguments: TArgType,
                                                          groupId: GroupId,
                                                          inputDescriptor: Option[Long]): (TReturnType, InvocationTokenWithCallbacks)

  def flushHistory(reference: FunctionReference)
}
