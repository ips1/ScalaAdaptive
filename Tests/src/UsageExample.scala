import scalaadaptive.core.options.Measurement

/**
  * Created by pk250187 on 3/19/17.
  */

object UsageExample {
  import scalaadaptive.core.macros.AdaptiveMacros._
  // -------------------------
  // Functional usage example:
  // -------------------------
  def selectionSort(data: List[Int]): List[Int] = ???
  def quickSort(data: List[Int]): List[Int] = ???
  def bubbleSort(data: List[Int]): List[Int] = ???
  val sort = selectionSort _ or quickSort or bubbleSort by (_.size) using Measurement.RunTime
  val sort2 = (selectionSort _).or(quickSort).or(bubbleSort).by(_.size).using(Measurement.RunTime)

  val chain = selectionSort _ andThen quickSort andThen bubbleSort

  // ------------------
  // OOP usage example:
  // ------------------
  object Sortable {
    import scalaadaptive.api.Implicits._

    implicit class SortableList[Int](list: List[Int]) {
      private def selectionSort(): List[Int] = ???
      private def quickSort(): List[Int] = ???
      private def bubbleSort(): List[Int] = ???
      val sort = selectionSort _ or quickSort or bubbleSort by (() => list.size) using Measurement.RunTime
    }
  }
}
