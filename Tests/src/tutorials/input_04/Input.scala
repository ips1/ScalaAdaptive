package tutorials.input_04

import com.sun.deploy.config.DefaultConfig

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.ConsoleLogging
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 7/16/17.
  */
object Input {
  import scalaadaptive.api.Implicits._

  def fibonacciRecursive(i: Int): Int = {
    def fib(i: Int): Int = {
      if (i == 0) 0
      else if (i == 1) 1
      else fib(i - 1) + fib(i - 2)
    }

    fib(i)
  }

  def fibonacciIterative(i : Int): Int = {
    var first = 0
    var second = 1
    Seq.range(0, i).foreach(i => {
      val third = first + second
      first = second
      second = third
    })
    first
  }

  // Using the "by" method, the input descriptor of an input is specified and the selection will be done
  // using given selector.
  val fibonacci = fibonacciRecursive _ or fibonacciIterative by (i => i)

  def main(args: Array[String]): Unit = {
    Seq.range(0, 100).foreach(i => {
      val n = Random.nextInt(40)
      println(s"$n: ${fibonacci(n)}")
    })
  }
}
