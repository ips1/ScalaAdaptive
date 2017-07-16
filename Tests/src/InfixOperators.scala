/**
  * Created by Petr Kubat on 4/22/17.
  */
object InfixOperators {
  class RichInt {
    def plus(other: RichInt) = ???
    def +(other: RichInt) = ???
  }

  val x = new RichInt()
  val y = new RichInt()
  //val res = x.plus(y)
  val res = x + y

  def method1(x: Int): Int = ???
  def method2(x: Int): Int = ???


}
