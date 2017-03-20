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
  val sort = selectionSort _ or quickSort or bubbleSort by (_.size) using Measurement.RunTime
  val sort2 = (selectionSort _).or(quickSort).or(bubbleSort).by(_.size).using(Measurement.RunTime)

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