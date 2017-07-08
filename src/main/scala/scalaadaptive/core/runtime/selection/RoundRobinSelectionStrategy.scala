package scalaadaptive.core.runtime.selection

import scala.collection.mutable
import scalaadaptive.core.runtime.history.HistoryKey
import java.time
import java.time.{Instant, ZonedDateTime}

import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 4/29/17.
  */
class RoundRobinSelectionStrategy[TMeasurement](val logger: Logger) extends SelectionStrategy[TMeasurement] {
  private def currentTime = ZonedDateTime.now.toInstant

  private val lastRuns: mutable.HashMap[HistoryKey, Instant] = new mutable.HashMap[HistoryKey, Instant]()

  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): HistoryKey = {
    logger.log("Selecting using RoundRobinSelector")
    val selected = records.minBy(r => lastRuns.getOrElse(r.key, Instant.MIN))
    lastRuns.put(selected.key, currentTime)
    selected.key
  }
}
