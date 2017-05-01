import scala.util.Random

/**
  * Created by pk250187 on 4/29/17.
  */
class TestRunner {
  val bigDataSize = 2000
  val smallDataSize = 20
  val testRunCount = 4
  val runCount = 100
  val step = 2
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

  def runIncrementalTest(testFnc: List[Int] => List[Int]): Unit = {
    Seq.range(0, testRunCount).foreach(k =>
      Seq.range(0, runCount).foreach(i =>
        Seq.range(0, 2).foreach(j => {
          val data = customData(smallDataSize + i * 2)
          println(s"Running test no: ${i} on ${data.size}")
          val res = testFnc(data)
          //assert(isOrdered(res))
        })
      )
    )
  }
}
