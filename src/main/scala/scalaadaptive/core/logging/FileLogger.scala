package scalaadaptive.core.logging

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

/**
  * Created by Petr Kubat on 4/1/17.
  *
  * [[Logger]] that logs into a file.
  *
  * @param fileName Full name of the file to log to.
  *
  */
class FileLogger(fileName: String) extends Logger {
  // TODO: Create the path first?
  // TODO: Replace file name with File?
  private val fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName))))

  override def write(message: String): Unit =  {
    fileWriter.println(message)
    fileWriter.flush()
  }
  // TODO: close the file?
  // The file will get closed by itself upon leaving the program
}
