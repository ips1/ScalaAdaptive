package scalaadaptive.core.runtime.selection

import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 3/19/17.
  *
  * The trait representing a strategy for selecting the most suitable function given a set of historical
  * measurements ([[RunHistory]]) and an input descriptor. The goal is to select the function with best expected
  * performance on given input. The implementation can use any approach in the selection process and can optimize
  * using different observations included in TMeasurement.
  *
  * In general, two types of SelectionStrategies can exist:
  * - input based strategies, i.e. strategies that use the input descriptors of the historical runs for the decision
  * - mean based strategies, i.e. strategies that do not require input descriptors and work only with means
  *
  * @tparam TMeasurement The type of a function run measurement, can contain data used to evaluate function runs.
  *                      A Long type is used for simple run time measurement.
  *
  */
trait SelectionStrategy[TMeasurement] {
  /**
    * Selects an option to run given a set of set of historical measurement sequences ([[RunHistory]]), one for each
    * function. Can optimize for various aspects.
    *
    * @param records Sequence of run histories, one for each function. The run histories contain function identifier
    *                and the group ID.
    * @param inputDescriptor Optional input descriptor for current input - we want to optimize for this input. In case
    *                        of None, we suppose that the function performances do not depend on the input.
    * @return The [[HistoryKey]] of selected function as present in its [[RunHistory]]
    */
  def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey
}
