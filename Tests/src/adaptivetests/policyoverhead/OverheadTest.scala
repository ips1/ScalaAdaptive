package adaptivetests.policyoverhead

import adaptivetests.testmethods.TestMethods

import scalaadaptive.core.functions.policies.AlwaysUseLastPolicy

/**
  * Created by pk250187 on 6/5/17.
  */
object OverheadTest {
  private def printMs(timeNs: Long) = {
    println(s"${timeNs.toDouble / (1000 * 1000)}ms")
  }

  def main(args: Array[String]): Unit = {
    val methods = new TestMethods()

    import scalaadaptive.api.Implicits._
    val useLastFunction = methods.fastMethod _ or methods.slowMethod withPolicy new AlwaysUseLastPolicy
    // Last should be the first method if no statistics are present

    val runCount = 100

    val totalNormalTime = Seq.range(0, runCount).map(i => {
      val startTime = System.nanoTime()
      methods.fastMethod(List(1))
      System.nanoTime() - startTime
    }).sum

    val totalUseLastTime = Seq.range(0, runCount).map(i => {
      val startTime = System.nanoTime()
      useLastFunction(List(1))
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
