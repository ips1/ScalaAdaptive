package scalaadaptive.core.functions

import scalaadaptive.api.grouping.GroupId
import scalaadaptive.core.performance.PerformanceProvider
import scalaadaptive.core.functions.references.FunctionReference

/**
  * Created by pk250187 on 5/27/17.
  */
class RunData(val selectedFunction: FunctionReference,
              val groupId: GroupId,
              val inputDescriptor: Option[Long],
              val performance: PerformanceProvider)
