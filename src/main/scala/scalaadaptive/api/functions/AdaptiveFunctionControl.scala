package scalaadaptive.api.functions

import scalaadaptive.api.policies.Policy

/**
  * Created by Petr Kubat on 6/28/17.
  *
  * A trait with common analytics functionality for the [[AdaptiveFunction0]] and corresponding types.
  *
  */
trait AdaptiveFunctionControl {
  /**
    * Creates a debug string that describes the adaptive function.
    * @return The debug string.
    */
  def toDebugString: String

  /**
    * Removes all records from the active run history of all implementations from the adaptive function.
    *
    * Beware that if the default history storage ([[scalaadaptive.api.options.Storage.Storage]]) is set to global,
    * this action will affect all the other adaptive functions that contain the same implementation.
    */
  def flushHistory(): Unit

  /**
    * Forces given [[Policy]] as the current [[Policy]] to the adaptive function.
    * @param policy The [[Policy]] to be set as the active one.
    */
  def setPolicy(policy: Policy): Unit

  /**
    * Sets the default [[Policy]] as the current [[Policy]].
    */
  def resetPolicy(): Unit
}
