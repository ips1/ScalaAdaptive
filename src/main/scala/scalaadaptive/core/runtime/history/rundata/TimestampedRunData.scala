package scalaadaptive.core.runtime.history.rundata

import java.time.Instant

/**
  * Created by pk250187 on 5/7/17.
  */
class TimestampedRunData[TMeasurement](val inputDescriptor: Option[Long],
                                       val unwrappedTime: Instant,
                                       val measurement: TMeasurement) extends RunData[TMeasurement] {
  override val time: Option[Instant] = Some(unwrappedTime)
}
