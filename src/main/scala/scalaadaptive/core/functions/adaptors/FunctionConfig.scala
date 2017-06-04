package scalaadaptive.core.functions.adaptors

import java.time.Duration

import scalaadaptive.api.options.Selection.Selection
import scalaadaptive.api.options.Storage.Storage
import scalaadaptive.core.functions.policies.Policy

/**
  * Created by pk250187 on 5/8/17.
  */
class FunctionConfig(val selection: Selection,
                     val storage: Storage,
                     val duration: Option[Duration],
                     val closureReferences: Boolean,
                     val startPolicy: Policy) {
  def selectUsing(newSelection: Selection) = new FunctionConfig(newSelection, storage, duration, closureReferences, startPolicy)
  def storeUsing(newStorage: Storage) = new FunctionConfig(selection, newStorage, duration, closureReferences, startPolicy)
  def limitedTo(newDuration: Duration) = new FunctionConfig(selection, storage, Some(newDuration), closureReferences, startPolicy)
  def asClosures(newClosureReferences: Boolean) = new FunctionConfig(selection, storage, duration, newClosureReferences, startPolicy)
  def withPolicy(newPolicy: Policy) = new FunctionConfig(selection, storage, duration, closureReferences, newPolicy)
}