package scalaadaptive.analytics

import java.time.Instant

import scalaadaptive.core.functions.identifiers.FunctionIdentifier

/**
  * Created by Petr Kubat on 5/20/17.
  *
  * A record representing one run of the combined function ([[scalaadaptive.api.adaptors.MultiFunction0]] and
  * corresponding types).
  *
  * @param created A timestamp of record creation (i.e. function invocation)
  * @param selectedFunction The identifier of the function selected
  * @param inputDescriptor The input descriptor which was used for the run
  * @param runTime The run time of the selected function (without selection and other framework overhead, does not
  *                depend on the TMeasurement type used)
  * @param overheadTime The time spent on the selection, evaluation and storing overhead. Note that it does not contain
  *                     the overhead spent on policy evaluation and statistics update.
  * @param overheadPercentage The percentage that the overhead took of the entire execution process.
  */
class AnalyticsRunRecord(val created: Instant,
                         val selectedFunction: FunctionIdentifier,
                         val inputDescriptor: Option[Long],
                         val runTime: Long,
                         val overheadTime: Long,
                         val overheadPercentage: Double)
