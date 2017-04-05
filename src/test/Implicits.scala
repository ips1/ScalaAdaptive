package test

import functionadaptors.Implicits._

import scala.collection.mutable

/**
  * Created by pk250187 on 4/2/17.
  */
object Implicits {
  def implicitFun1[A](list: List[A])(implicit ord: A => Ordered[A]): List[A] = ???
  def implicitFun2[A](list: List[A])(implicit ord: A => Ordered[A]): List[A] = ???

  //def fun: (List[A]) => List[String] = implicitFun1 _ or implicitFun2

  def map1[A, B](list: List[A], fun: (A) => B): List[B] = list.map(fun)
  def map2[A, B](list: List[A], fun: (A) => B): List[B] =  {
    val result = new mutable.MutableList[B]()
    for (x <- list) {
      result += fun(x)
    }
    result.toList
  }

  val mapMember = map1 _

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
