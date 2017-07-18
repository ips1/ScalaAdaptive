package tutorials.limitingage

import java.time.Duration

/**
  * Created by Petr Kubat on 7/18/17.
  */
object LimitingAge {
  import scalaadaptive.api.Implicits._

  val fastHello = () => println("Hello World!")
  val slowHello = () => {
    Thread.sleep(10)
    println("Sloooooow Hello World!")
  }

  // Maximal duration 5s of historical records
  val hello = fastHello or slowHello limitedTo Duration.ofSeconds(5)

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 100) { hello() }
    // Wait until records expire
    Thread.sleep(5000)
    // Has to collect data again
    for (i <- 0 until 100) { hello() }
  }
}
