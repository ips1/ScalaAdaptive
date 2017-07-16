package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * A serializer of the function evaluation data (see
  * [[scalaadaptive.core.runtime.history.evaluation.data.EvaluationData]]). Specific implementations will have to be
  * provided for various [[TMeasurement]] types.
  *
  * The basic implementation for Long values is [[LongEvaluationDataSerializer]].
  *
  */
trait EvaluationDataSerializer[TMeasurement] {
  /** Serializes the [[scalaadaptive.core.runtime.history.evaluation.data.EvaluationData]] into a String */
  def serializeEvaluationData(run: EvaluationData[TMeasurement]): String
  /** Deserializes the [[scalaadaptive.core.runtime.history.evaluation.data.EvaluationData]] from a String */
  def deserializeEvaluationData(string: String): Option[EvaluationData[TMeasurement]]
}
