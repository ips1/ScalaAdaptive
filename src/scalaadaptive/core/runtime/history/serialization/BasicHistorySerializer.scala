package scalaadaptive.core.runtime.history.serialization

import java.io.{FileOutputStream, PrintWriter}
import java.nio.file.Path

import scalaadaptive.core.runtime.history.{HistoryKey, RunData}

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

  override def serializeMultipleRuns(key: HistoryKey, runs: Seq[RunData[Long]]): Unit = {
    val file = getFilePath(key).toFile
    file.getParentFile.mkdirs()
    val writer = new PrintWriter(new FileOutputStream(file, true))
    runs.foreach(r => writer.println(runDataSerializer.serializeRunData(r)))
    writer.close()
  }

  override def deserializeHistory(key: HistoryKey): Option[Seq[RunData[Long]]] = {
    val file = getFilePath(key).toFile
    if (!file.exists || !file.canRead) {
      return None
    }

    val history = new ArrayBuffer[RunData[Long]]()
    for (line <- Source.fromFile(file).getLines()) {
      runDataSerializer.deserializeRunData(line) match {
        case Some(runData) => history.append(runData)
        case _ =>
      }
    }

    Some(history)
  }
}
