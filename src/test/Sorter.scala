package test

import functionadaptors.Implicits._

/**
  * Created by petrkubat on 29/01/2017.
  */
object Sorter {
  def maximum(xs: List[Int]): List[Int] =
    (List(xs.head) /: xs.tail) {
      (ys, x) =>
        if(x > ys.head) (x :: ys)
        else            (ys.head :: x :: ys.tail)
    }

  def selectionSort(xs: List[Int]): List[Int] = {
    println("Running selectionSort")
    def selectionSortHelper(xs: List[Int], accumulator: List[Int]): List[Int] =
      if(xs.isEmpty) accumulator
      else {
        val ys = maximum(xs)
        selectionSortHelper(ys.tail, ys.head :: accumulator)
      }

    selectionSortHelper(xs, Nil)
  }

  def quickSort(ls: List[Int]): List[Int] = {
    println("Running quickSort")

    def sort(ls: List[Int])(parent: List[Int]): List[Int] = {
      if (ls.size <= 1) ls ::: parent else {
        val pivot = ls.head

        val (less, equal, greater) = ls.foldLeft((List[Int](), List[Int](), List[Int]())) {
          case ((less, equal, greater), e) => {
            if (e < pivot)
              (e :: less, equal, greater)
            else if (e == pivot)
              (less, e :: equal, greater)
            else
              (less, equal, e :: greater)
          }
        }

        sort(less)(equal ::: sort(greater)(parent))
      }
    }

    sort(ls)(Nil)
    return ls
  }

  def standardSort(list: List[Int]): List[Int] = {
    list.sortBy(x => x)
  }

  val sort = selectionSort _ or standardSort
}

