package scalaadaptive.core.adaptors

import scalaadaptive.api.adaptors.MultiFunctionCommon
import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.{Adaptive, FunctionRunner}

/**
  * Created by pk250187 on 5/14/17.
  */
trait FunctionAdaptorCommon extends MultiFunctionCommon {
  protected def functionReferences: Iterable[FunctionReference]
  protected val runner: FunctionRunner

  override def toDebugString: String =
    functionReferences.map(r => r.toString).mkString(", ")

  override def flushHistory(): Unit =
    functionReferences.foreach(r => runner.flushHistory(r))
}
