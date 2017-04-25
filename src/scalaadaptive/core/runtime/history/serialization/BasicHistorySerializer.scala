package scalaadaptive.core.runtime.history.serialization

import java.io.{FileOutputStream, PrintWriter}
import java.nio.file.Path

import scalaadaptive.core.runtime.history.{FullRunHistory, HistoryKey, RunData}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

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
    val file = getFilePath(key).toFile
    file.getParentFile.mkdirs()
    val writer = new PrintWriter(new FileOutputStream(file, true))
    writer.println(runDataSerializer.serializeRunData(run))
    writer.close()
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
