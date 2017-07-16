package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by Petr Kubat on 4/23/17.
  */
trait FileNameForKeyProvider {
  def getFileNameForHistoryKey(key: HistoryKey): String
}
