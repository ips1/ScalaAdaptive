import scalaadaptive.core.functions.adaptors.Conversions
import scalaadaptive.core.functions.identifiers.MethodNameIdentifier

/**
  * Created by Petr Kubat on 6/29/17.
  */
object IdentifierTest {
  def method(): Unit = println("HelloWorld")

  class TestClass {
    def method(): Unit = println("HelloWorld")
  }

  def main(args: Array[String]): Unit = {
    val function1 = method _
    val function2 = method _

    val list = Seq.range(0, 3).map(i => () => method()).toList

    val function = () => println("Hello World!")

    import scalaadaptive.api.Implicits._
    import scalaadaptive.api.Implicits

    val target = new TestClass()

    val combined1 = target.method _ or function2

    val combined2 = { () => target.method() } or function2

    val combined3 = Implicits.toMultiFunction0({ () => target.method() }) or function2

    val combined4 = Conversions.toAdaptor({ () => target.method() }, MethodNameIdentifier(this.getClass.getName + ".method")) or function2

    println(function1 == function2)
  }
}
