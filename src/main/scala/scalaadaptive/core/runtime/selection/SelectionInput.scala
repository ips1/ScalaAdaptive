package scalaadaptive.core.runtime.selection

import java.time.Duration

import scalaadaptive.api.grouping.Group
import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.core.functions.identifiers.IdentifiedFunction

/**
  * Created by Petr Kubat on 7/16/17.
  *
  * The input data of the selection process.
  *
  * @param options The function options to select from.
  * @param arguments The arguments to invoke the selected function with.
  * @param groupId The group the input falls in.
  * @param inputDescriptor The input descriptor of the input (or None in case the descriptor function is not defined).
  * @param limitedBy The maximal duration of historical records to take into consideration.
  * @param selection The type of selection strategy to use in the process.
  * @tparam TArgType The tupled arguments of the function.
  * @tparam TReturnType The return type of the function.
  */
class SelectionInput[TArgType, TReturnType](val options: Seq[IdentifiedFunction[TArgType, TReturnType]],
                                            val arguments: TArgType,
                                            val groupId: Group,
                                            val inputDescriptor: Option[Long],
                                            val limitedBy: Option[Duration],
                                            val selection: Selection)
