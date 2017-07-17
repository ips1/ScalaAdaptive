package scalaadaptive.api.functions

import java.time.Duration

import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.api.policies.Policy
import scalaadaptive.core.runtime.selection.strategies.SelectionStrategy

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A trait with common functionality for the [[AdaptiveFunction0]] and corresponding types.
  *
  * @tparam TArgType Tupled argument type of the function
  * @tparam TRetType Return type of the function
  * @tparam TAdaptiveFunctionType Type of the adaptive function that is extending this trait
  *
  */
trait AdaptiveFunctionCommon[TArgType, TRetType, TAdaptiveFunctionType]
  extends AdaptiveFunctionControl with AdaptiveFunctionAnalytics {

  /**
    * Creates a new adaptive function with the [[scalaadaptive.api.options.Selection.Selection]] configuration updated.
    * The corresponding [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] from the
    * [[scalaadaptive.core.configuration.Configuration]] will be used.
    * @param newSelection The new value for the [[scalaadaptive.api.options.Selection.Selection]] configuration.
    * @return A completely new instance of [[TAdaptiveFunctionType]] with the configuration updated.
    */
  def selectUsing(newSelection: Selection): TAdaptiveFunctionType

  /**
    * Creates a new adaptive function with the [[scalaadaptive.api.options.Storage.Storage]] configuration updated.
    * The corresponding [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] from the
    * [[scalaadaptive.core.configuration.Configuration]] will be used.
    * @param newStorage The new value for the [[scalaadaptive.api.options.Storage.Storage]] configuration.
    * @return A completely new instance of [[TAdaptiveFunctionType]] with the configuration updated.
    */
  def storeUsing(newStorage: Storage): TAdaptiveFunctionType

  /**
    * Creates a new adaptive function with the maximum history record age configuration updated. Only the records that
    * are younger than given duration will be taken into account for this adaptive function decision making.
    * @param newDuration The maximal history record age.
    * @return A completely new instance of [[TAdaptiveFunctionType]] with the configuration updated.
    */
  def limitedTo(newDuration: Duration): TAdaptiveFunctionType

  /**
    * Creates a new adaptive function with the new starting [[scalaadaptive.api.policies.Policy]] set.
    * @param newPolicy The new starting [[scalaadaptive.api.policies.Policy]].
    * @return A completely new instance of [[TAdaptiveFunctionType]] with the configuration updated.
    */
  def withPolicy(newPolicy: Policy): TAdaptiveFunctionType

  /**
    * Creates a new adaptive function with changed closure identifier preference. By default, the adaptive function
    * uses method name identifiers or custom identifier if they are available. By setting the closure identification
    * to true, this gets overriden and closure identifier will be used all the time.
    * @param closureIdentification True if the closure identifiers should be used all the time, false if the default
    *                              approach should be taken.
    * @return A completely new instance of [[TAdaptiveFunctionType]] with the configuration updated.
    */
  def asClosures(closureIdentification: Boolean): TAdaptiveFunctionType

  /**
    * Trains the adaptive function by executing each one of the implementations on each one of the train inputs.
    * The runs get evaluated and the history is stored. The return values are thrown away.
    *
    * @param data The sequence of training inputs.
    */
  def train(data: Seq[TArgType]): Unit
}
