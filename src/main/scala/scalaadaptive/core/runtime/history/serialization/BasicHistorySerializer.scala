package scalaadaptive.core.runtime.history.serialization

import java.io.{FileOutputStream, IOException, PrintWriter}
import java.nio.file.Path

import scalaadaptive.core.runtime.history.HistoryKey
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.evaluation.data.EvaluationData

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * Basic implementation of [[HistorySerializer]]. Serializes and persists all records immediately into a file.
  *
  * @param rootPath The path of the directory where to store the data.
  * @param fileNameForKeyProvider The file name provider.
  * @param runDataSerializer The serializer which is used to transform
  *                          [[scalaadaptive.core.runtime.history.evaluation.data.EvaluationData]] to and from strings.
  * @param logger The logger to report serialization failures.
  */
class BasicHistorySerializer(private val rootPath: Path,
                             private val fileNameForKeyProvider: FileNameForKeyProvider,
                             private val runDataSerializer: EvaluationDataSerializer[Long],
                             private val logger: Logger)
  extends HistorySerializer[Long] {

  private def getFilePath(key: HistoryKey): Path =
    rootPath.resolve(fileNameForKeyProvider.getFileNameForHistoryKey(key))

  override def serializeMultipleRuns(key: HistoryKey, runs: Seq[EvaluationData[Long]]): Unit = {
    val file = getFilePath(key).toFile

    try {
      file.getParentFile.mkdirs()
      val writer = new PrintWriter(new FileOutputStream(file, true))
      runs.foreach(r => writer.println(runDataSerializer.serializeEvaluationData(r)))
      writer.close()
    } catch {
      case e: Exception => logger.log(s"Failed to serialize run result: ${e.getMessage}")
    }
  }

  override def deserializeHistory(key: HistoryKey): Option[Seq[EvaluationData[Long]]] = {
    val file = getFilePath(key).toFile
    if (!file.exists || !file.canRead) {
      return None
    }

    val history = new ArrayBuffer[EvaluationData[Long]]()

    try {
      for (line <- Source.fromFile(file).getLines()) {
        runDataSerializer.deserializeEvaluationData(line) match {
          case Some(runData) => history.append(runData)
          case _ =>
        }
      }
    } catch {
      case e: Exception =>
        logger.log(s"Failed to deserialize run result: ${e.getMessage}")
        return None
    }

    Some(history)
  }

  override def removeHistory(key: HistoryKey): Unit = {
    logger.log(s"Trying to remove history file of $key")

    val file = getFilePath(key).toFile
    try {
      if (file.exists && file.canWrite) {
        file.delete()
      }
    } catch {
      case e: Exception =>
        logger.log(s"Failed to remove history file: ${e.getMessage}")
    }
  }
}
