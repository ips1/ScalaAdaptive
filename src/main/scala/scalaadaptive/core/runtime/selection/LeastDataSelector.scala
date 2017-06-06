package scalaadaptive.core.runtime.selection

import java.time.{Instant, ZonedDateTime}

import scala.collection.mutable
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.HistoryKey
import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 5/20/17.
  */
class LeastDataSelector[TMeasurement](val logger: Logger) extends RunSelector[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): RunHistory[TMeasurement] = {
    logger.log("Selecting using LeastDataSelector")
    records.minBy(r => r.runCount)
  }
}
