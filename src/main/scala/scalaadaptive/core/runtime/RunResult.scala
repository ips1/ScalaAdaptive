package scalaadaptive.core.runtime

import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.references.FunctionReference

/**
  * Created by pk250187 on 5/20/17.
  */
class RunResult[TResultType](val value: TResultType,
                             val runData: RunData)
