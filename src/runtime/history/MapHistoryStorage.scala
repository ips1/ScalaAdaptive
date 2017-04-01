package runtime.history

import references.FunctionReference

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/21/17.
  */
class MapHistoryStorage[TMeasurement](val newHistoryFactory: (FunctionReference) => RunHistory[TMeasurement])
  extends HistoryStorage[TMeasurement] {
  val histories: mutable.HashMap[HistoryKey, RunHistory[TMeasurement]] =
    new mutable.HashMap[HistoryKey, RunHistory[TMeasurement]]()

  override def getHistory(key: HistoryKey): RunHistory[TMeasurement] =
    histories.getOrElseUpdate(key, newHistoryFactory(key.function))
}
