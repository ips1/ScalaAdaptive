/**
  * Created by Petr Kubat on 4/11/17.
  */
object Overloads {

  def nonOverloaded(x: Int) = s"Int: $x"

  def method(x: Int) = s"Int: $x"
  def method(x: String) = s"String: $x"
  def method(x: Double) = s"Double: $x"
  def method(x: Int, y: Int, z: Int) = s"($x, $y, $z)"

  val fun1 = nonOverloaded _
  // Doesn't compile:
  // val fun2 = method _
}
