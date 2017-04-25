package scalaadaptive.core.runtime.history

import scalaadaptive.core.references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
trait RunHistory[TMeasurement] {
  def reference: FunctionReference
  def key: HistoryKey
  def runCount: Int
  def runItems: Seq[RunData[TMeasurement]]
  def average(): Double
  def best(): Double
  def applyNewRun(runResult: RunData[TMeasurement]): RunHistory[TMeasurement]
}
