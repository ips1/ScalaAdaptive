package scalaadaptive.analytics

/**
  * Created by Petr Kubat on 6/5/17.
  */
trait AnalyticsSerializer {
  def serializeRecord(record: AnalyticsRecord): String
}
