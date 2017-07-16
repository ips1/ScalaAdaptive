package scalaadaptive.core.runtime.history.evaluation.data

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * The average evaluation for an input descriptor. Holds the actual average and the count that it was computed
  * from.
  *
  */
class GroupedEvaluationData[TMeasurement](val averageMeasurement: TMeasurement, val runCount: Int)
