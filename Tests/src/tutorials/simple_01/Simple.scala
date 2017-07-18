package tutorials.simple_01

/**
  * Created by Petr Kubat on 7/16/17.
  */
object Simple {
  import scalaadaptive.api.Implicits._

  val fastHello = () => println("Hello World!")
  val slowHello = () => {
    Thread.sleep(10)
    println("Sloooooow Hello World!")
  }

  val hello = fastHello or slowHello

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 100) { hello() }
  }
}
