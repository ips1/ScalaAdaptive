import scalaadaptive.api.Implicits._

class A
class B

/**
  * Created by pk250187 on 4/1/17.
  */
object CovarianceContravariance {
//  def fun1(arg: AnyRef): String = "I say nuttin!"
//  def fun2(arg: String): AnyRef = if (arg.nonEmpty) arg else List.empty[String]
//  def fun3(text: String): String = s"I say: $text"

  class TestClass(val i: Int)

  abstract class Covariant[-T]() {
    def run()
  }

  class Normal[T](val data: T, var fun: (T) => Unit) extends Covariant[T]() {
    def doStuff(f: (T) => Unit) = { fun = f }
    override def run() = fun(data)
  }

  val fun1: (Any) => String = ???
  val fun2: (String) => Any = ???
  val fun3: (String) => String = ???

  //val fun4 = fun3 or fun2
  val fun5 = fun2 or fun3
  val fun6 = fun3 or fun1
  //val fun7 = fun1 or fun3
  val generalFun3 = fun1.asInstanceOf[(Any) => Any] or fun2
  var generalFun2 = fun2 or fun1

  val funInt: (Int) => String = ???
  val funString: (String) => Int = ???
  val generalFun = funInt.asInstanceOf[(Int with String) => Any] or funString

  def processData1(data: Iterable[Int]): Int = ???
  def processData2(data: List[Int]): Int = ???

  def processCombined = processData2 _ or processData1

  def sort1(data: Iterable[Int]): Iterable[Int] = ???
  def sort2(data: List[Int]): List[Int] = ???
  val sortCombined = (sort2 _).asInstanceOf[(List[Int]) => Iterable[Int]] or sort1


  def main(args: Array[String]): Unit = {
    println("Hi")
    val test = new Normal[Any]("Ahoj", x => println(x))
    val test2: Covariant[Any] = test
    val test3: Covariant[TestClass] = test2
    test3 match {
      case test4: Normal[TestClass] => {
        test4.doStuff(x => println(x.i))
        test4.run()
      }
    }
//    test3.run("Ahoj")
  }

  def method1[T](arg: T) = s"$arg"
  def method2[T](arg: T) = s"$arg"

//  def method[T](arg: T) = (method1[T] _ or method2[T])(arg)

  def fun8(arg: Any): Any = ???
//  val fun9: (String) => Any = fun8 _ or fun3

  def funA(arg: A): A = ???
  def funB(arg: B): B = ???
//  val funAB: (B with A) => Object = funA _ or funB
}
