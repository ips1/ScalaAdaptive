package scalaadaptive.core.runtime.history.runhistory

import org.apache.commons.math3.stat.descriptive.StatisticalSummary

import scalaadaptive.core.functions.identifiers.FunctionIdentifier
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.{EvaluationData, GroupedEvaluationData}
import scalaadaptive.math.SimpleTestableRegression

/**
  * Created by Petr Kubat on 3/21/17.
  *
  * A type that represents sequence of all the historical runs of a function.
  *
  * Contains the runs and some more statistical information that can be either computed or cached.
  *
  */
trait RunHistory[TMeasurement] {
  /** The identifier of the function */
  def identifier: FunctionIdentifier = key.functionId
  /** The key of the function */
  def key: HistoryKey

  // Basic methods:
  /** Number of all the runs in this history **/
  def runCount: Int
  /** Sequence of all the run evaluations in this history **/
  def runItems: Iterable[EvaluationData[TMeasurement]]

  /** Minimal input descriptor from the whole history */
  def minDescriptor: Option[Long]
  /** Maximal input descriptor from the whole history */
  def maxDescriptor: Option[Long]

  /**
    * Adds a new run to the function history.
    * @param runResult The run evaluation to be added.
    * @return New run history (can be either the same instance or a different one).
    */
  def applyNewRun(runResult: EvaluationData[TMeasurement]): RunHistory[TMeasurement]

  /**
    * Creates a new [[RunHistory]] by taking the last added items while a filter condition is true.
    * @param filter The filter condition.
    * @return New [[RunHistory]] containing only filtered sequence from the end, in the same order.
    */
  def takeWhile(filter: EvaluationData[TMeasurement] => Boolean): RunHistory[TMeasurement]

  /**
    * Filters the [[RunHistory]] records. Returns a new instance.
    * @param filter The filter condition.
    * @return New instance of [[RunHistory]]
    */
  def filter(filter: EvaluationData[TMeasurement] => Boolean): RunHistory[TMeasurement]

  // Cacheable or computable methods
  /** Returns a regression built upon the historical data (either computed or cached) */
  def runRegression: SimpleTestableRegression
  /** Returns statistical summary of the historical data (either computed or cached) */
  def runStatistics: StatisticalSummary
  /** Returns average evaluation data for each input descriptor value (either computed or cached) */
  def runAveragesGroupedByDescriptor: Map[Long, GroupedEvaluationData[TMeasurement]]

  // Implemented methods
  import scalaadaptive.extensions.DoubleExtensions._
  /** Returns the mean from the evaluation data */
  def mean: Option[Double] = runStatistics.getMean.asOption
  /** Returns the maximum from the evaluation data */
  def max: Option[Double] = runStatistics.getMax.asOption
}
