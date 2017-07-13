/**
  * Created by pk250187 on 7/12/17.
  */
object ArgsList {
  def method(a: Int, b:Int)(c: Int)(d: Int): Int = {
    a + b + c + d
  }

  import scalaadaptive.core.macros.MacroUtils.printAst

  val test: (Int, Int) => (Int) => (Int) => Int = method _

  def method2(i: Int): Int = ???
  def method2(s: String): Int = ???

  def main(args: Array[String]): Unit = {
    printAst(method _)

    val function: (String) => Int = method2
    def useFunction(fun: (Int) => Int): Unit = ???
    useFunction(method2)
  }
}
