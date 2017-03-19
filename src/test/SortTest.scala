package test

import functionadaptors.FunctionAdaptor1
import functionadaptors.Implicits._
import options.Measurement

/**
  * Created by pk250187 on 3/19/17.
  */

class Foo[T, R] {
  def run(fun: T => R) = ???
}

object SortTest {
  def selectionSort(data: List[Int]): List[Int] = ???
  def quickSort(data: List[Int]): List[Int] = ???
  def bubbleSort(data: List[Int]): List[Int] = ???
  import functionadaptors._
  val sort = selectionSort _ or quickSort _ or bubbleSort _ by (_.size) using Measurement.RunTime
  //val sort2 = (selectionSort _).or(quickSort).or(bubbleSort).by(_.size).using(Measurement.RunTime)
  val chain = (selectionSort _).andThen(quickSort).andThen(bubbleSort)

  def run[T, R](fun: T => R) = ???
  run(selectionSort)

  val x = new Foo[List[Int], List[Int]]()
  x.run(selectionSort)

  val adaptor: FunctionAdaptor1[List[Int], List[Int]] = new FunctionAdaptor1(List())
  adaptor.or(quickSort _)

  List(List(1,2), List(10, 2)).map(selectionSort)
}

// OOP Example:
object Sortable {
  implicit class SortableList[T](list: List[T]) {
    def selectionSort(): List[Int] = ???
    def quickSort(): List[Int] = ???
    def bubbleSort(): List[Int] = ???
    val sort = selectionSort _ or quickSort _ or bubbleSort _ by (() => list.size) using Measurement.RunTime
  }
}