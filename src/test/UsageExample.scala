package test

import functionadaptors.Implicits._
import options.Measurement

/**
  * Created by pk250187 on 3/19/17.
  */

object UsageExample {
  // -------------------------
  // Functional usage example:
  // -------------------------
  def selectionSort(data: List[Int]): List[Int] = ???
  def quickSort(data: List[Int]): List[Int] = ???
  def bubbleSort(data: List[Int]): List[Int] = ???
  val sort = selectionSort _ or quickSort _ or bubbleSort _ by (_.size) using Measurement.RunTime
  val sort2 = (selectionSort _).or(quickSort _).or(bubbleSort _).by(_.size).using(Measurement.RunTime)
  // Doesn't compile:
  // val sort3 = selectionSort _ or quickSort or bubbleSort by (_.size) using Measurement.RunTime
  // val sort4 = (selectionSort _).or(quickSort).or(bubbleSort).by(_.size).using(Measurement.RunTime)

  // Why do the following examples compile? It looks like the same kind of conversion / eta expansion
  val chain = (selectionSort _).andThen(quickSort).andThen(bubbleSort)

  def run[T, R](fun: T => R) = ???
  run(selectionSort)

  class Foo[T, R] {
    def run(fun: T => R) = ???
  }

  val x = new Foo[List[Int], List[Int]]()
  x.run(selectionSort)

  List(List(1,2), List(10, 2)).map(selectionSort)

  // ------------------
  // OOP usage example:
  // ------------------
  object Sortable {
    implicit class SortableList[Int](list: List[Int]) {
      private def selectionSort(): List[Int] = ???
      private def quickSort(): List[Int] = ???
      private def bubbleSort(): List[Int] = ???
      val sort = selectionSort _ or quickSort _ or bubbleSort _ by (() => list.size) using Measurement.RunTime
    }
  }
}