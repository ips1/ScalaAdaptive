package adaptivetests.sorting

import scalaadaptive.api.options.{Selection, Storage}

/**
  * Created by petrkubat on 29/01/2017.
  */
class Sorter {
  def maximum(xs: List[Int]): List[Int] =
    (List(xs.head) /: xs.tail) {
      (ys, x) =>
        if(x > ys.head) (x :: ys)
        else            (ys.head :: x :: ys.tail)
    }

  def selectionSort(xs: List[Int]): List[Int] = {
    println(">> Running selectionSort")
    def selectionSortHelper(xs: List[Int], accumulator: List[Int]): List[Int] =
      if(xs.isEmpty) accumulator
      else {
        val ys = maximum(xs)
        selectionSortHelper(ys.tail, ys.head :: accumulator)
      }

    selectionSortHelper(xs, Nil)
  }

  def quickSort(ls: List[Int]): List[Int] = {
    println(">> Running quickSort")

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

  def radixSort(list: List[Int]): List[Int] = ???

  def bubbleSort[A](list: List[A])(implicit ord: A => Ordered[A]): List[A] = {
    def sort(as: List[A], bs: List[A]): List[A] =
      if (as.isEmpty) bs
      else bubble(as, Nil, bs)

    def bubble(as: List[A], zs: List[A], bs: List[A]): List[A] = as match {
      case h1 :: h2 :: t =>
        if (h1 > h2) bubble(h1 :: t, h2 :: zs, bs)
        else bubble(h2 :: t, h1 :: zs, bs)
      case h1 :: Nil => sort(zs, h1 :: bs)
    }

    sort(list, Nil)
  }

  def doStuff[A <% Ordered[A]](list: List[A]) = ???

  def standardSort(list: List[Int]): List[Int] = {
    println(">> Running standardSort")
    list.sortBy(x => x)
  }

  val list: List[Int] = List(1,2,3)

  //doStuff(list)

  import scalaadaptive.api.Implicits._

  //val sort = radixSort _ or bubbleSort[Int]

  val sort = standardSort _ or selectionSort by (_.size) selectUsing Selection.Predictive //using Storage.Persistent

  //val sort = selectionSort _ or bubbleSort by (_.size)

  //val sort2 = bubbleSort _ or selectionSort
}
