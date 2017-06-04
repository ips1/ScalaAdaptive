package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.core.references.FunctionReference
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{GroupedEvaluationData, EvaluationData}

/**
  * Created by pk250187 on 3/21/17.
  */
trait RunHistory[TMeasurement] {
  def reference: FunctionReference = key.function
  def key: HistoryKey
  def runCount: Int
  def runStatistics: StatisticalSummary
  def runItems: Iterable[EvaluationData[TMeasurement]]
  def runAveragesGroupedByDescriptor: Map[Option[Long], GroupedEvaluationData[TMeasurement]]
  def average(): Option[Double]
  def best(): Option[Double]
  def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement]
  def takeWhile(filter: EvaluationData[TMeasurement] => Boolean): RunHistory[TMeasurement]
}
