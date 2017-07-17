package scalaadaptive.core.runtime.history.serialization

import java.time.Instant

import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData
import scalaadaptive.extensions.OptionSerializer

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * An implementation of [[EvaluationDataSerializer]] that serializes the data as csv records using specified separator.
  * Works with Long measurement type.
  *
  */
class LongEvaluationDataSerializer(private val separator: Char) extends EvaluationDataSerializer[Long] {
  override def serializeEvaluationData(run: EvaluationData[Long]): String = {
    val timeString = run.time.toString
    val descriptorString = OptionSerializer.serializeOption(run.inputDescriptor)

    s"$timeString$separator$descriptorString$separator${run.measurement}"
  }

  override def deserializeEvaluationData(string: String): Option[EvaluationData[Long]] = {
    val parts = string.split(separator)
    if (parts.length != 3) {
      return None
    }

    try {
      val time = parts(0)
      val inputDescriptor = OptionSerializer.deserizalizeOption[Long](parts(1), s => s.toLong)
      val measurement = parts(2).toLong

      if (time.isEmpty) None
      else Some(new EvaluationData[Long](inputDescriptor, Instant.parse(time), measurement))
    }
    catch {
      case _: NumberFormatException => None
    }
  }
}
