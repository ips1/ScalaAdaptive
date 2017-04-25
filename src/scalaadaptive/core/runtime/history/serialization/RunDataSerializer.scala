package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.runtime.history.RunData

/**
  * Created by pk250187 on 4/23/17.
  */
trait RunDataSerializer[TMeasurement] {
  def serializeRunData(run: RunData[TMeasurement]): String
  def deserializeRunData(string: String): Option[RunData[TMeasurement]]
}
