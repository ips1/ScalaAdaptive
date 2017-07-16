package scalaadaptive.core.runtime.history.evaluation.data

import java.time.Instant

/**
  * Created by Petr Kubat on 5/7/17.
  */
class EvaluationData[TMeasurement](val inputDescriptor: Option[Long],
                                   val time: Instant,
                                   val measurement: TMeasurement)
