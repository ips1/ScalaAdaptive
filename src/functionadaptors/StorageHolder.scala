package functionadaptors

import runtime.history.HistoryStorage

/**
  * Created by pk250187 on 4/22/17.
  */
trait StorageHolder {
  private val storage: HistoryStorage
}
