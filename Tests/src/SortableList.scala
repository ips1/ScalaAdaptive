/**
  * Created by Petr Kubat on 4/5/17.
  */

import adaptivetests.sorting.Sorter
import scalaadaptive.api.Implicits._
object Sortable {
  implicit class SortableList[T](list: List[T])(implicit ord: T => Ordered[T]) {
    private def standardSort(): List[T] = list.sorted
    private def bubbleSort(): List[T] = new Sorter().bubbleSort(list)

    val sort = standardSort _ or bubbleSort
  }
}
