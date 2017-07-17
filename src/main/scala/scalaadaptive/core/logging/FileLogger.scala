package scalaadaptive.core.logging

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

/**
  * Created by Petr Kubat on 4/1/17.
  *
  * [[Logger]] that logs into a file. If the file cannot be created for writing, does nothing.
  *
  * @param fileName Full name of the file to log to.
  *
  */
class FileLogger(fileName: String) extends Logger {

  private def initFileWriter: Option[PrintWriter] = try {
    val file = new File(fileName)
    file.mkdirs()
    Some(new PrintWriter(new BufferedWriter(new FileWriter(file))))
  } catch {
    case _: Exception => None
  }

  private val fileWriter = initFileWriter

  override def write(message: String): Unit =  {
    fileWriter.foreach(_.println(message))
    fileWriter.foreach(_.flush())
  }

  // The file will get closed by itself upon leaving the program
}
