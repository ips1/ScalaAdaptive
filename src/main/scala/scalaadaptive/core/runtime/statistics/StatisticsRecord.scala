package scalaadaptive.core.runtime.statistics

import scalaadaptive.core.references.FunctionReference

/**
  * Created by pk250187 on 5/20/17.
  */
class StatisticsRecord(val function: FunctionReference,
                       val runTime: Long,
                       val overheadTime: Long,
                       val overheadPercentage: Double)
