/**
  * Created by Petr Kubat on 4/2/17.
  */
object Methods {
  def main(args: Array[String]): Unit = {
    class Class {
      def method(arg: String): String = s"Called method($arg) on $this"

      val methodFunction: (String) => String = arg => method(arg)

      val composed: (String) => String = (method _).andThen(method)

      def foo() = List("One", "Two", "Three").map(method)
    }
    val obj = new Class()
    println(obj.method("Hello"))

    val function: (String) => Unit = arg => println(obj.method(arg))
    function("Hello again!")
    List("One", "Two", "Three").foreach(function)
    obj.foo().foreach(println)
  }
}
