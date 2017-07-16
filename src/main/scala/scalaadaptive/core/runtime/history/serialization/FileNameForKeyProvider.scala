package scalaadaptive.core.runtime.history.serialization

import scalaadaptive.core.runtime.history.HistoryKey

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * A class that generates file names for [[scalaadaptive.core.runtime.history.HistoryKey]] instances.
  * The file name should be valid AND unique for each [[scalaadaptive.core.runtime.history.HistoryKey]].
  *
  */
trait FileNameForKeyProvider {
  def getFileNameForHistoryKey(key: HistoryKey): String
}
