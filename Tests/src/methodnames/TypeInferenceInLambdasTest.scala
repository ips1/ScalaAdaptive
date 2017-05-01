package methodnames

/**
  * Created by pk250187 on 5/1/17.
  */
object TypeInferenceInLambdasTest {
  def method(i: Int): Int = ???

  // The following compiles well, although IntelliJ can't parse it
  val function = { i => method(i) }

  def main(args: Array[String]): Unit = {
    function(5)
  }
}
