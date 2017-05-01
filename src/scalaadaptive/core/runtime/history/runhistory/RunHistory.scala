package scalaadaptive.core.runtime.history.runhistory

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.{GroupedRunData, HistoryKey, RunData}

/**
  * Created by pk250187 on 3/21/17.
  */
trait RunHistory[TMeasurement] {
  def reference: FunctionReference = key.function
  def key: HistoryKey
  def runCount: Int
  def runItems: Iterable[RunData[TMeasurement]]
  def runAveragesGroupedByDescriptor: Map[Long, GroupedRunData[TMeasurement]]
  def average(): Option[Double]
  def best(): Option[Double]
  def applyNewRun(runResult: RunData[TMeasurement]): RunHistory[TMeasurement]
}
