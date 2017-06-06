package scalaadaptive.core.runtime.selection

import scalaadaptive.core.runtime.history.runhistory.RunHistory

/**
  * Created by pk250187 on 3/19/17.
  */
trait RunSelector[TMeasurement] {
  def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Option[Long]): RunHistory[TMeasurement]
}
