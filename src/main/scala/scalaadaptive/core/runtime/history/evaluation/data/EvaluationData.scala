package scalaadaptive.core.runtime.history.evaluation.data

import java.time.Instant

/**
  * Created by Petr Kubat on 5/7/17.
  *
  * An evaluation data record for a function. Represents evaluation of a single function run performed at a certain
  * point in time.
  *
  */
class EvaluationData[TMeasurement](val inputDescriptor: Option[Long],
                                   val time: Instant,
                                   val measurement: TMeasurement)
