package runtime.history
import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/21/17.
  */
trait HistoryStorage[TMeasurement] {
  def getHistory(key: HistoryKey): RunHistory[TMeasurement]
}
