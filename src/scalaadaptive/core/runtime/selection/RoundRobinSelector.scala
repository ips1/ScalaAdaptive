package scalaadaptive.core.runtime.selection

import scala.collection.mutable
import scalaadaptive.core.runtime.history.{HistoryKey, RunHistory}
import java.time
import java.time.{Instant, ZonedDateTime}

/**
  * Created by pk250187 on 4/29/17.
  */
class RoundRobinSelector[TMeasurement] extends RunSelector[TMeasurement] {
  private def currentTime = ZonedDateTime.now.toInstant

  private val lastRuns: mutable.HashMap[HistoryKey, Instant] = new mutable.HashMap[HistoryKey, Instant]()

  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Long): RunHistory[TMeasurement] = {
    val selected = records.minBy(r => lastRuns.getOrElse(r.key, Instant.MIN))
    lastRuns.put(selected.key, currentTime)
    selected
  }
}
