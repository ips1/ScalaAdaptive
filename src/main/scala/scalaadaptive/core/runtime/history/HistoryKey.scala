package scalaadaptive.core.runtime.history

import scalaadaptive.core.runtime.grouping.GroupId
import scalaadaptive.core.functions.references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
case class HistoryKey(function: FunctionReference,
                      groupId: GroupId)
