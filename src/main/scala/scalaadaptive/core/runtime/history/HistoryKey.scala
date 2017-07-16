package scalaadaptive.core.runtime.history

import scalaadaptive.api.grouping.Group
import scalaadaptive.core.functions.identifiers.FunctionIdentifier

/**
  * Created by Petr Kubat on 3/21/17.
  *
  * An identifier of a [[scalaadaptive.core.runtime.history.runhistory.RunHistory]], a sequence of historical
  * measurement unique for a function and a group of inputs.
  *
  */
case class HistoryKey(functionId: FunctionIdentifier,
                      groupId: Group)
