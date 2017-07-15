package scalaadaptive.core.functions

import scalaadaptive.api.grouping.Group
import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.functions.identifiers.FunctionIdentifier

/**
  * Created by pk250187 on 5/27/17.
  */
class RunData(val selectedFunction: FunctionIdentifier,
              val groupId: Group,
              val inputDescriptor: Option[Long],
              val performance: PerformanceProvider)
