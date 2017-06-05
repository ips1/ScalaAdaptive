package scalaadaptive.analytics

/**
  * Created by pk250187 on 6/5/17.
  */
class CsvAnalyticsSerializer extends AnalyticsSerializer {
  val separator = ';'
  override def serializeRecord(record: AnalyticsRecord): String = {
    val strings = List(
      record.created,
      record.inputDescriptor.getOrElse(""),
      record.function.toString,
      record.runTime,
      record.overheadTime,
      record.overheadPercentage)
    strings.mkString(separator.toString)
  }
}
