package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.api.policies.Policy

/**
  * Created by pk250187 on 5/8/17.
  */
class FunctionConfig(val selection: Option[Selection],
                     val storage: Storage,
                     val duration: Option[Duration],
                     val closureIdentifier: Boolean,
                     val startPolicy: Policy) {
  def selectUsing(newSelection: Selection) = new FunctionConfig(Some(newSelection), storage, duration, closureIdentifier, startPolicy)
  def storeUsing(newStorage: Storage) = new FunctionConfig(selection, newStorage, duration, closureIdentifier, startPolicy)
  def limitedTo(newDuration: Duration) = new FunctionConfig(selection, storage, Some(newDuration), closureIdentifier, startPolicy)
  def asClosures(newClosureIdentifier: Boolean) = new FunctionConfig(selection, storage, duration, newClosureIdentifier, startPolicy)
  def withPolicy(newPolicy: Policy) = new FunctionConfig(selection, storage, duration, closureIdentifier, newPolicy)
}