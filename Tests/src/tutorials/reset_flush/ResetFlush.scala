package tutorials.reset_flush

import scalaadaptive.api.Adaptive

/**
  * Created by Petr Kubat on 7/17/17.
  */
object ResetFlush {
  import scalaadaptive.api.Implicits._

  val fastHello = () => println("Hello World!")
  val slowHello = () => {
    Thread.sleep(10)
    println("Sloooooow Hello World!")
  }

  val hello = fastHello or slowHello

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 100) { hello() }

    // Resetting the whole framework
    println("Resetting")
    Adaptive.reset()
    for (i <- 0 until 100) { hello() }

    // Flushing function history
    println("Flushing slowHello")
    slowHello.flushHistory()
    for (i <- 0 until 100) { hello() }

    println("Flushing fastHello")
    fastHello.flushHistory()
    for (i <- 0 until 100) { hello() }
  }
}
