package scalaadaptive.core.adaptors

import java.time.Duration

import scalaadaptive.core.options.Selection.Selection
import scalaadaptive.core.options.Storage.Storage

/**
  * Created by pk250187 on 5/8/17.
  */
class AdaptorConfig(val selection: Selection,
                    val storage: Storage,
                    val duration: Option[Duration],
                    val closureReferences: Boolean) {
  def selectUsing(newSelection: Selection) = new AdaptorConfig(newSelection, storage, duration, closureReferences)
  def storeUsing(newStorage: Storage) = new AdaptorConfig(selection, newStorage, duration, closureReferences)
  def limitedTo(newDuration: Duration) = new AdaptorConfig(selection, storage, Some(newDuration), closureReferences)
  def asClosures(closureIdentification: Boolean) = new AdaptorConfig(selection, storage, duration, closureIdentification)
}