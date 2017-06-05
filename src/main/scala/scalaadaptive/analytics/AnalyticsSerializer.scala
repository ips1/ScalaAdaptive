package scalaadaptive.analytics

/**
  * Created by pk250187 on 6/5/17.
  */
trait AnalyticsSerializer {
  def serializeRecord(record: AnalyticsRecord): String
}
