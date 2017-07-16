package scalaadaptive.analytics

/**
  * Created by Petr Kubat on 6/5/17.
  *
  * A type for serializing an analytics record into a String. Note that we want the result to be both readable and
  * possible to process and parse by different applications.
  *
  */
trait AnalyticsSerializer {
  /**
    * Serializes one analytics record.
    * @param record Record to be serialized
    * @return String containing the serialized record
    */
  def serializeRecord(record: AnalyticsRunRecord): String
}
