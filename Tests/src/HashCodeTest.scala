/**
  * Created by pk250187 on 7/12/17.
  */
object HashCodeTest {
  def createAdder(n: Int): Function1[Int, Int] = (i) => i + n

  def main(args: Array[String]): Unit = {
    val add1 = createAdder(1)
    val add2 = createAdder(2)

    println(add1(1))
    println(add2(1))

    println(add1.hashCode())
    println(add2.hashCode())

    val newAdder = createAdder(1)

    println(newAdder.hashCode())
  }
}
