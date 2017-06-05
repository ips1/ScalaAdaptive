package scalaadaptive.analytics

import java.time.Instant

import scalaadaptive.core.functions.references.FunctionReference

/**
  * Created by pk250187 on 5/20/17.
  */
class AnalyticsRecord(val created: Instant,
                      val function: FunctionReference,
                      val inputDescriptor: Option[Long],
                      val runTime: Long,
                      val overheadTime: Long,
                      val overheadPercentage: Double)
