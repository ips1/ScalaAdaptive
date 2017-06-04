package scalaadaptive.core.logging

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

/**
  * Created by pk250187 on 4/1/17.
  */
class FileLogger(fileName: String) extends Logger {
  private val fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName))))
  override def write(message: String): Unit =  {
    fileWriter.println(message)
    fileWriter.flush()
  }
  // TODO: close the file?
  // The file will get closed by itself upon leaving the program
}