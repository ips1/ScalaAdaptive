/**
  * Created by pk250187 on 4/8/17.
  */
object Generics {
  import scalaadaptive.api.Implicits._
  def defaultCount[A](list: List[A], item: A) = list.count(_ == item)
  def customCount[A](list: List[A], item: A) = list.filter(_ == item).map((i) => 1).sum

  class ListTools[A] {
    val count = defaultCount[A] _ or customCount[A]
  }

  def count[A](list: List[A], item: A) = (defaultCount _ or customCount)(list, item)

  val testCount = defaultCount _

  def percentageFunc[A](list: List[A], cond: A => Boolean) = list.count(cond) / list.size
  def percentageImp[A](list: List[A], cond: A => Boolean) = {
    var total = 0
    var matching = 0
    list.foreach({ item =>
      total += 1
      if (cond(item)) matching += 1
    })
    matching / total
  }

  implicit class RichList[A](val list: List[A]) {
    //val percentage =
  }

  def makeTuple[A, B](a: A, b: B): (A, B) = (a, b)
  val tupelize = makeTuple _

  def print[A, B](a: A, b: B) = { println(a); println(b) }

  val printFun = print _

  val fun1: (Nothing, Nothing) => (Nothing, Nothing) = makeTuple
  val fun2: (Int, String) => (Int, String) = makeTuple[Int, String]

  def main(args: Array[String]): Unit = {
    printFun(125, "ahoj")
    //val res = tupelize(1, 2)
    //println(res)
    println(testCount.getClass.getTypeName)
    println(testCount(List(1, 2, 1, 1), "a"))
    //println(new ListTools[Int].count(List(1, 2, 1, 1), 1))

    println(testCount(List("a", "b", "a"), "a"))
  }
}
