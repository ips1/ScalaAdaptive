package scalaadaptive.core.runtime.history.rundata
import java.time.Instant

/**
  * Created by pk250187 on 5/7/17.
  */
class ArtificialRunData[TMeasurement](val inputDescriptor: Option[Long],
                                      val measurement: TMeasurement) extends RunData[TMeasurement] {
  override val time: Option[Instant] = None
}
