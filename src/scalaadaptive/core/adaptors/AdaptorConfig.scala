package scalaadaptive.core.adaptors

import java.time.Duration

import scalaadaptive.core.options.Storage.Storage

/**
  * Created by pk250187 on 5/8/17.
  */
class AdaptorConfig(val storage: Storage,
                    val duration: Option[Duration],
                    val closureReferences: Boolean) {

  def using(newStorage: Storage) = new AdaptorConfig(newStorage, duration, closureReferences)
  def limitedTo(newDuration: Duration) = new AdaptorConfig(storage, Some(newDuration), closureReferences)
  def asClosures(closureIdentification: Boolean) = new AdaptorConfig(storage, duration, closureIdentification)
}