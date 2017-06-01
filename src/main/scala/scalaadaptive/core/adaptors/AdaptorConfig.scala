package scalaadaptive.core.adaptors

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage.Storage
import scalaadaptive.core.runtime.policies.Policy

/**
  * Created by pk250187 on 5/8/17.
  */
class AdaptorConfig(val selection: Selection,
                    val storage: Storage,
                    val duration: Option[Duration],
                    val closureReferences: Boolean,
                    val startPolicy: Policy) {
  def selectUsing(newSelection: Selection) = new AdaptorConfig(newSelection, storage, duration, closureReferences, startPolicy)
  def storeUsing(newStorage: Storage) = new AdaptorConfig(selection, newStorage, duration, closureReferences, startPolicy)
  def limitedTo(newDuration: Duration) = new AdaptorConfig(selection, storage, Some(newDuration), closureReferences, startPolicy)
  def asClosures(newClosureReferences: Boolean) = new AdaptorConfig(selection, storage, duration, newClosureReferences, startPolicy)
  def withPolicy(newPolicy: Policy) = new AdaptorConfig(selection, storage, duration, closureReferences, newPolicy)
}