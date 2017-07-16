package scalaadaptive.analytics

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A simple implementation of [[AnalyticsSerializer]] that serializes the members of the analytics record into a csv
  * line separated by semicolons.
  *
  */
class CsvAnalyticsSerializer extends AnalyticsSerializer {
  val separator = ';'
  override def serializeRecord(record: AnalyticsRunRecord): String = {
    val strings = List(
      record.created,
      record.inputDescriptor.getOrElse(""),
      record.selectedFunction.toString,
      record.runTime,
      record.overheadTime,
      record.overheadPercentage)
    strings.mkString(separator.toString)
  }
}
