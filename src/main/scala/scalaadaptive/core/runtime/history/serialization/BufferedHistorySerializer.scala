package scalaadaptive.core.runtime.history.serialization

import scala.collection.mutable
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * A [[HistorySerializer]] wrapper that buffer the items until buffer size is reached and then serializes them all
  * at once using the internal serializer.
  *
  * @param innerSerializer The internal serializer used to serialize buffered batches of evaluation data.
  * @param bufferLimit The size limit of the buffer, after reaching it, the buffer is flushed and all the data
  *                    serialized.
  *
  */
class BufferedHistorySerializer[TMeasurement](val innerSerializer: HistorySerializer[TMeasurement],
                                              val bufferLimit: Int)
  extends HistorySerializer[TMeasurement] {
  private val serializationBuffer = new mutable.HashMap[HistoryKey, mutable.ArrayBuffer[EvaluationData[TMeasurement]]]()

  override def serializeMultipleRuns(key: HistoryKey, runs: Seq[EvaluationData[TMeasurement]]): Unit = {
    val keyBuffer = serializationBuffer.getOrElseUpdate(key, new mutable.ArrayBuffer[EvaluationData[TMeasurement]]())
    keyBuffer.appendAll(runs)
    if (keyBuffer.size > bufferLimit) {
      innerSerializer.serializeMultipleRuns(key, keyBuffer)
      keyBuffer.clear()
    }
  }

  // Delegation:
  override def deserializeHistory(key: HistoryKey): Option[Seq[EvaluationData[TMeasurement]]] =
    innerSerializer.deserializeHistory(key)

  override def removeHistory(key: HistoryKey): Unit = innerSerializer.removeHistory(key)
}
