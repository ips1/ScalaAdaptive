package scalaadaptive.core.runtime.history.serialization

import java.time.Instant

import scalaadaptive.core.runtime.history.rundata.{ArtificialRunData, RunData, TimestampedRunData}

/**
  * Created by pk250187 on 4/23/17.
  */
class LongRunDataSerializer(private val separator: Char) extends RunDataSerializer[Long] {

  override def serializeRunData(run: RunData[Long]): String = {
    val timeString = run.time match {
      case Some(time) => time.toString
      case _ => ""
    }

    s"$timeString$separator${run.inputDescriptor}$separator${run.measurement}"
  }

  override def deserializeRunData(string: String): Option[RunData[Long]] = {
    val parts = string.split(separator)
    if (parts.length != 3) {
      return None
    }

    try {
      val time = parts(0)
      val inputDescriptor = parts(1).toLong
      val measurement = parts(2).toLong

      val data =
        if (time.isEmpty) new ArtificialRunData[Long](inputDescriptor, measurement)
        else new TimestampedRunData[Long](inputDescriptor, Instant.parse(time), measurement)

      Some(data)
    }
    catch {
      case ex: NumberFormatException => None
    }
  }
}
