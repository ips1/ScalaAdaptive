package runtime.history

import grouping.GroupId
import references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
case class HistoryKey(function: FunctionReference,
                      groupId: GroupId)
