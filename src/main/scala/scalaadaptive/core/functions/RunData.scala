package scalaadaptive.core.functions

import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.functions.references.FunctionReference

/**
  * Created by pk250187 on 5/27/17.
  */
class RunData(val selectedFunction: FunctionReference,
              val inputDescriptor: Option[Long],
              val performance: PerformanceProvider)
