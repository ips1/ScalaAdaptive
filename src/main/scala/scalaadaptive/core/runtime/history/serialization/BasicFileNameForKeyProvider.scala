package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.api.grouping.{GroupId, Group, NoGroup}
import scalaadaptive.core.functions.identifiers.{ClosureIdentifier, CustomIdentifier, FunctionIdentifier, MethodNameIdentifier}
import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * Basic implementation of [[FileNameForKeyProvider]]. Uses underscores to divide parts of the file name, which is
  * composed from the function identifier (each type has different prefix) and the group ID.
  *
  */
class BasicFileNameForKeyProvider extends FileNameForKeyProvider {
  private def getFunctionString(function: FunctionIdentifier): String = function match {
    case MethodNameIdentifier(name) => s"method_$name"
    case ClosureIdentifier(name) => s"closure_$name"
    case CustomIdentifier(name) => s"custom_$name"
  }

  private def getGroupIdString(groupId: Group): String = groupId match {
    case GroupId(id) => s"$id"
    case NoGroup() => ""
  }

  override def getFileNameForHistoryKey(key: HistoryKey): String = key match {
    case HistoryKey(function, groupId) => s"${getFunctionString(function)}_${getGroupIdString(groupId)}.hist"
  }
}
