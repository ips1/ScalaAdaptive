package runtime.history.serialization

import grouping.GroupId
import references.{ClosureNameReference, FunctionReference, MethodNameReference}
import runtime.history.HistoryKey

/**
  * Created by pk250187 on 4/23/17.
  */
class BasicFileNameForKeyProvider extends FileNameForKeyProvider {
  private def getFunctionString(function: FunctionReference): String = function match {
    case MethodNameReference(name) => s"method_$name"
    case ClosureNameReference(name) => s"closure_$name"
  }

  private def getGroupIdString(groupId: GroupId): String = groupId match {
    case GroupId(id) => s"$id"
  }

  override def getFileNameForHistoryKey(key: HistoryKey): String = key match {
    case HistoryKey(function, groupId) => s"${getFunctionString(function)}_${getGroupIdString(groupId)}"
  }
}
