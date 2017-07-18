package tutorials.sharing_history

/**
  * Created by Petr Kubat on 7/18/17.
  */
object History {
  import scalaadaptive.api.Implicits._

  class Hello {
    def fastHello(): Unit = println("Hello World!")
    def slowHello(): Unit =  {
      Thread.sleep(10)
      println("Sloooooow Hello World!")
    }
    def superSlowHello(): Unit = {
      Thread.sleep(100)
      println("Suuuuupeeeeeer sloooooooo....")
    }
  }

  val helloInstance = new Hello
  val hello = helloInstance.fastHello _ or helloInstance.slowHello
  val differentHelloInstance = new Hello
  val slowHello = differentHelloInstance.slowHello _ or helloInstance.superSlowHello

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 100) { hello() }
    for (i <- 0 until 100) { slowHello() }
  }
}
