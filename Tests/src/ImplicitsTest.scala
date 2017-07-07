import scala.collection.mutable

/**
  * Created by pk250187 on 4/2/17.
  */
object ImplicitsTest {

  trait Incrementable[A] {
    def increment: A
  }

  implicit def intIsIncrementable(i: Int): Incrementable[Int] = new Incrementable[Int] {
    override def increment: Int = i + 1
  }

  implicit def stringIsIncrementable(i: String): Incrementable[String] = new Incrementable[String] {
    override def increment: String = i + "+"
  }

  def incrementValue[K, V](data: mutable.Map[K, V], key: K)
                          (implicit inc: V => Incrementable[V]) =
    data.update(key, data(key).increment)

  import scalaadaptive.api.Implicits._

  def sort1[A](list: List[A])(implicit ord: A => Ordered[A]): List[A] = ???
  def sort2[A](list: List[A])(implicit ord: A => Ordered[A]): List[A] = ???

  // Following line won't compile:
  //def implicitFun[A](list: List[A]): List[A] = (sort1[A] _ or sort2[A])(list)
  def implicitFun[A](list: List[A])(implicit ord: A => Ordered[A]): List[A] = (sort1[A] _ or sort2[A])(list)

//  def implicitFun3[A <% Ordered[A]](list: List[A]): List[A] = ???
//  def implicitFun4[A <% Ordered[A]](list: List[A]): List[A] = ???
//
//  def fun2 = implicitFun3 _ or implicitFun4

  //def fun: (List[A]) => List[String] = implicitFun1 _ or implicitFun2

  def defaultMap[A, B](list: List[A], fun: (A) => B): List[B] = list.map(fun)
  def iterativeMap[A, B](list: List[A], fun: (A) => B): List[B] =  {
    val result = new mutable.MutableList[B]()
    for (x <- list) {
      result += fun(x)
    }
    result.toList
  }

  class Sorter[A](implicit ord: A => Ordered[A]) {
    val sort = sort1[A] _ or sort2[A]
  }

  import scalaadaptive.api.Implicits._

  def map[A, B](list: List[A], fun: A => B): List[B] = (defaultMap[A, B] _ or iterativeMap[A, B])(list, fun)

  def main(args: Array[String]): Unit = {
    val map = collection.mutable.Map[String, String]()

    map += ("Test" -> "X")

    incrementValue(map, "Test")
    incrementValue(map, "Test")
    incrementValue(map, "Test")

    println(map("Test"))
  }

  //  def map = map1 _ or map2
//
//  def main(args: Array[String]): Unit = {
//    val runCount = 100
//    val initialList = Seq.range(0, 1000).toList
//    Seq.range(0, runCount).foreach(i => {
//      val result = map(initialList, x => x * 2)
//    })
//  }
}
