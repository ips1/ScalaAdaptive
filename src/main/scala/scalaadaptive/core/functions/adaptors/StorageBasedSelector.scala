package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.api.grouping.GroupId
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.core.functions.references.{FunctionReference, ReferencedFunction}
import scalaadaptive.core.runtime._
import scalaadaptive.core.runtime.invocationtokens.InvocationTokenWithCallbacks
import scalaadaptive.core.functions.RunResult

/**
  * Created by pk250187 on 4/22/17.
  */
class StorageBasedSelector(val storage: Storage) extends AdaptiveSelector {
  private val runner: AdaptiveSelector = storage match {
    case Storage.Local => AdaptiveInternal.createNewSelector()
    case _ => null
  }

  private def selectRunner: AdaptiveSelector =
    storage match {
      case Storage.Local => runner
      case Storage.Persistent => AdaptiveInternal.getSharedPersistentRunner
      case _ => AdaptiveInternal.getSharedRunner
    }

  // Delegations
  override def runOption[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                arguments: TArgType,
                                                groupId: GroupId,
                                                inputDescriptor: Option[Long],
                                                limitedBy: Option[Duration],
                                                selection: Selection): RunResult[TReturnType] =
    selectRunner.runOption(options, arguments, groupId, inputDescriptor, limitedBy, selection)

  override def runOptionWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                                  arguments: TArgType,
                                                                  groupId: GroupId,
                                                                  inputDescriptor: Option[Long],
                                                                  limitedBy: Option[Duration],
                                                                  selection: Selection): (TReturnType, InvocationTokenWithCallbacks) =
    selectRunner.runOptionWithDelayedMeasure(options, arguments, groupId, inputDescriptor, limitedBy, selection)

  override def flushHistory(reference: FunctionReference): Unit =
    selectRunner.flushHistory(reference)

  override def gatherData[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                 arguments: TArgType,
                                                 groupId: GroupId,
                                                 inputDescriptor: Option[Long]): RunResult[TReturnType] =
    selectRunner.gatherData(options, arguments, groupId, inputDescriptor)

  override def gatherDataWithDelayedMeasure[TArgType, TReturnType](options: Seq[ReferencedFunction[TArgType, TReturnType]],
                                                                   arguments: TArgType,
                                                                   groupId: GroupId,
                                                                   inputDescriptor: Option[Long]): (TReturnType, InvocationTokenWithCallbacks) =
    selectRunner.gatherDataWithDelayedMeasure(options, arguments, groupId, inputDescriptor)
}
