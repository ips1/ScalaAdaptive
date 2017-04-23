package runtime.history.serialization

import java.nio.file.Path

import runtime.history.{FullRunHistory, HistoryKey, RunData}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scalax.io._

/**
  * Created by pk250187 on 4/23/17.
  */
class BasicHistorySerializer(private val rootPath: Path,
                             private val fileNameForKeyProvider: FileNameForKeyProvider,
                             private val runDataSerializer: RunDataSerializer[Long])
  extends HistorySerializer[Long] {

  private def getFilePath(key: HistoryKey): Path =
    rootPath.resolve(fileNameForKeyProvider.getFileNameForHistoryKey(key))

  override def serializeNewRun(key: HistoryKey, run: RunData[Long]): Unit = {
    val file = Resource.fromFile(getFilePath(key).toFile)
    file.append(runDataSerializer.serializeRunData(run))
  }

  override def deserializeHistory(key: HistoryKey): Option[FullRunHistory[Long]] = {
    val file = getFilePath(key).toFile
    if (!file.exists || !file.canRead) {
      return None
    }

    // TODO: Add factory?
    var history = new FullRunHistory[Long](key, new ArrayBuffer[RunData[Long]]())
    for (line <- Source.fromFile(file).getLines()) {
      runDataSerializer.deserializeRunData(line) match {
        case Some(runData) => history.applyNewRun(runData)
        case _ =>
      }
    }

    return Some(history)
  }
}
