package adaptivetests.policyoverhead

/**
  * Created by pk250187 on 6/5/17.
  */
class OverheadUtils {
  private def printMs(timeNs: Long) = {
    println(s"${timeNs.toDouble / (1000 * 1000)}ms")
  }

  def performTest(runCount: Int,
                          data: List[Int],
                          normalFunction: (List[Int]) => List[Int],
                          customFunction: (List[Int]) => List[Int]): Unit = {
    val totalNormalTime = Seq.range(0, runCount).map(i => {
      val startTime = System.nanoTime()
      normalFunction(data)
      System.nanoTime() - startTime
    }).sum

    val totalUseLastTime = Seq.range(0, runCount).map(i => {
      val startTime = System.nanoTime()
      customFunction(data)
      System.nanoTime() - startTime
    }).sum

    val avgNormalTime = totalNormalTime / runCount
    val avgUseLastTime = totalUseLastTime / runCount

    val avgOverhead = avgUseLastTime - avgNormalTime

    printMs(avgNormalTime)
    printMs(avgUseLastTime)
    printMs(avgOverhead)
  }
}
