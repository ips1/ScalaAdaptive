package scalaadaptive.core.runtime.history

import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.identifiers.FunctionIdentifier

/**
  * Created by Petr Kubat on 3/21/17.
  */
case class HistoryKey(functionId: FunctionIdentifier,
                      groupId: Group)
