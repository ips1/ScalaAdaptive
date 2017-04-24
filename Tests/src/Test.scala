import scala.util.Random

/**
  * Created by pk250187 on 3/19/17.
  */
object Test {
  val bigDataSize = 2000
  val smallDataSize = 20
  val runCount = 200
  lazy val smallData = Seq.fill(smallDataSize)(Random.nextInt).toList
  lazy val bigData = Seq.fill(bigDataSize)(Random.nextInt).toList

  def customData(size: Int): List[Int] = Seq.fill(size)(Random.nextInt).toList

  def isOrdered(l : List[Int]): Boolean = (l, l.tail).zipped.forall(_ <= _)

  def runTest(testFnc: (List[Int]) => List[Int]): Unit = {
    Seq.range(0, runCount).foreach(i => {
      val data = if (i % 2 == 0) smallData else bigData
      println(s"Running test no: ${i} on ${data.size}")
      val res = testFnc(data)
      assert(isOrdered(res))
    })
  }

  def runIncrementalTest(testFnc: (List[Int]) => List[Int]): Unit = {
    Seq.range(0, runCount).foreach(i => {
      Seq.range(0, 1).foreach(j => {
        val data = customData(smallDataSize + i)
        println(s"Running test no: ${i} on ${data.size}")
        val res = testFnc(data)
        assert(isOrdered(res))
      })
    })
  }

  def main(args: Array[String]): Unit = {
    import Sortable._

    //runTest(l => l.sort())
    val sorter = new Sorter()
    //runTest(l => sorter.sort(l))
    Seq.range(0, 1).foreach(i => {
      runIncrementalTest(l => sorter.sort(l))
    })
  }
}
