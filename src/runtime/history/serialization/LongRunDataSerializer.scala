package runtime.history.serialization

import runtime.history.RunData

/**
  * Created by pk250187 on 4/23/17.
  */
class LongRunDataSerializer(private val separator: Char) extends RunDataSerializer[Long] {

  override def serializeRunData(run: RunData[Long]): String = {
    s"${run.inputDescriptor}$separator${run.measurement}"
  }

  override def deserializeRunData(string: String): Option[RunData[Long]] = {
    val parts = string.split(separator)
    if (parts.size != 2) {
      return None
    }

    try {
      val inputDescriptor = parts(0).toLong
      val measurement = parts(1).toLong
      Some(new RunData[Long](inputDescriptor, measurement))
    }
    catch {
      case ex: NumberFormatException => None
    }
  }
}
