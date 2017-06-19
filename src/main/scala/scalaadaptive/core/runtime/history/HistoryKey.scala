package scalaadaptive.core.runtime.history

import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.functions.references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
case class HistoryKey(function: FunctionReference,
                      groupId: GroupId)
