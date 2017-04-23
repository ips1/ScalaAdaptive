package runtime.history.serialization

import runtime.history.HistoryKey

/**
  * Created by pk250187 on 4/23/17.
  */
trait FileNameForKeyProvider {
  def getFileNameForHistoryKey(key: HistoryKey): String
}
