package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by pk250187 on 3/21/17.
  */
trait RunHistory[TMeasurement] {
  def identifier: FunctionIdentifier = key.functionId
  def key: HistoryKey

  // Basic methods:
  def runCount: Int
  def runItems: Iterable[EvaluationData[TMeasurement]]

  def minDescriptor: Option[Long]
  def maxDescriptor: Option[Long]

  def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement]
  def takeWhile(filter: EvaluationData[TMeasurement] => Boolean): RunHistory[TMeasurement]
  def filter(filter: EvaluationData[TMeasurement] => Boolean): RunHistory[TMeasurement]

  // Cacheable or computable methods
  def average(): Option[Double]
  def best(): Option[Double]

  def runRegression: SimpleTestableRegression
  def runStatistics: StatisticalSummary
  def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]]
}
