package test

/**
  * Created by pk250187 on 4/8/17.
  */
object Generics {
  import functionadaptors.Implicits._

  def defaultCount[A](list: List[A], item: A) = list.count(_ == item)
  def customCount[A](list: List[A], item: A) = list.filter(_ == item).map((i) => 1).sum

  class ListTools[A] {
    val count = defaultCount[A] _ or customCount[A]
  }

  def count[A](list: List[A], item: A) = (defaultCount _ or customCount)(list, item)

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
  val fun1: (Nothing, Nothing) => (Nothing, Nothing) = makeTuple
  val fun2: (Int, String) => (Int, String) = makeTuple[Int, String]

}
