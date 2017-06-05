package scalaadaptive.analytics

import scalaadaptive.core.functions.statistics.StatisticsRecord

/**
  * Created by pk250187 on 6/5/17.
  */
trait AnalyticsSerializer {
  def serializeRecord(record: StatisticsRecord): String
}
