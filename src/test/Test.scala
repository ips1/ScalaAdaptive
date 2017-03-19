package test

import scala.util.Random

/**
  * Created by pk250187 on 3/19/17.
  */
object Test {
  val dataSize = 4000
  val runCount = 200
  lazy val data = Seq.fill(dataSize)(Random.nextInt).toList

  def isOrdered(l : List[Int]): Boolean = (l, l.tail).zipped.forall(_ <= _)

  def runTest(data: List[Int]): Unit = {
    Seq.range(0, runCount).foreach(i => {
      println("Running test no: " + i)
      val res = Sorter.sort(data)
      assert(isOrdered(res))
    })
  }

  def main(args: Array[String]): Unit = {
    runTest(data)
  }
}
