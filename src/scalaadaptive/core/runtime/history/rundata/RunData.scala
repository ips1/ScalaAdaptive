package scalaadaptive.core.runtime.history.rundata

import java.time.Instant

/**
  * Created by pk250187 on 3/21/17.
  */

trait RunData[TMeasurement] {
  val inputDescriptor: Option[Long]
  val time: Option[Instant]
  val measurement: TMeasurement
}