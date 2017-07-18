package tutorials.classmember_03

/**
  * Created by Petr Kubat on 7/16/17.
  */
object ClassMember {
  import scalaadaptive.api.Implicits._

  class Hello {
    def fastHello(): Unit = println("Hello World!")
    def slowHello(): Unit =  {
      Thread.sleep(10)
      println("Sloooooow Hello World!")
    }

    private val helloFun = fastHello _ or slowHello
    def hello(): Unit = helloFun()
  }

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 100) {
      // Note that the function run history survives the new instance creation
      val h = new Hello
      h.hello()
    }
  }
}
