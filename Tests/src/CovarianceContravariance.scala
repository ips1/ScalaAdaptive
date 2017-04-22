import functionadaptors.Implicits._

class A
class B

/**
  * Created by pk250187 on 4/1/17.
  */
object CovarianceContravariance {
//  def fun1(arg: AnyRef): String = "I say nuttin!"
//  def fun2(arg: String): AnyRef = if (arg.nonEmpty) arg else List.empty[String]
//  def fun3(text: String): String = s"I say: $text"

  def fun1(arg: Any): String = ???
  def fun2(arg: String): Any = ???
  def fun3(arg: String): String = ???

  val fun4 = fun3 _ or fun2
  val fun5 = fun2 _ or fun3
  val fun6 = fun3 _ or fun1
  val fun7 = fun1 _ or fun3

  def method1[T](arg: T) = s"$arg"
  def method2[T](arg: T) = s"$arg"

  def method[T](arg: T) = (method1[T] _ or method2[T])(arg)

  def fun8(arg: Any): Any = ???
  val fun9: (String) => Any = fun8 _ or fun3

  def funA(arg: A): A = ???
  def funB(arg: B): B = ???
  val funAB: (B with A) => Object = funA _ or funB
}
