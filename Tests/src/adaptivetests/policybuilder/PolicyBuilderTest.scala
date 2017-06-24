package adaptivetests.policybuilder

import scalaadaptive.api.policies.builder.BuilderImplicits
import scalaadaptive.api.policies.builder.BuilderImplicits.gatherData
import scalaadaptive.api.policies.{Policy, PolicyResult, StatisticDataProvider}

/**
  * Created by pk250187 on 6/23/17.
  */
class TestStatistics extends StatisticDataProvider {
  var totalRunCount = 0
  var totalGatherCount = 0
  var totalSelectCount = 0
  var streakLength = 0
  var totalTime = 0
  var totalFunctionTime = 0
  var totalOverheadTime = 0
  var totalGatherTime = 0
  var lastRunCount = 0
  var mostRunCount = 0

  override def getTotalRunCount: Long = totalRunCount
  override def getTotalGatherCount: Long = totalGatherCount
  override def getTotalSelectCount: Long = totalSelectCount
  override def getStreakLength: Long = streakLength
  override def getTotalTime: Long = totalTime
  override def getTotalFunctionTime: Long = totalFunctionTime
  override def getTotalOverheadTime: Long = totalOverheadTime
  override def getTotalGatherTime: Long = totalGatherTime
  override def getLastRunCount: Long = lastRunCount
  override def getMostRunCount: Long = mostRunCount
}

object PolicyBuilderTest {
  import scalaadaptive.api.policies.builder.BuilderImplicits._

  val myPolicy: Policy = BuilderImplicits produce PolicyResult.UseLast forever
  val myPolicy2: Policy = (
    selectNew until ((stats: StatisticDataProvider) => stats.getTotalRunCount > 500)
    andThen useMost forever
  )

  val useLastForever: Policy = useLast forever
  val conditional: Policy = (
    gatherData until (((stats: StatisticDataProvider) => stats.getTotalRunCount) growsBy 50)
    andThen selectNew until (((stats: StatisticDataProvider) => stats.getTotalRunCount) growsBy 100)
    andThenIf ((stats: StatisticDataProvider) => stats.getStreakLength >= 20) goTo useLastForever
    andThenRepeat
  )

  val checking: Policy =
    (
      (selectNew once)
        andThenIf ((stats: StatisticDataProvider) => stats.getStreakLength >= 20) goTo (useLast forever)
        andThenIf ((stats: StatisticDataProvider) => stats.getMostRunCount.toDouble / stats.getTotalRunCount >= 0.8) goTo (useMost forever)
      andThenRepeat
    )

  val andPolicy: Policy = {
    (
      selectNew until
        (((stats: StatisticDataProvider) => stats.getMostRunCount.toDouble / stats.getTotalRunCount) growsBy 0.1
        and (((stats: StatisticDataProvider) => stats.getMostRunCount) growsBy 50))
      andThen useMost until ((() => System.nanoTime()) growsBy 10000000)
      andThenRepeat
    )
  }

  def test1(): Unit = {
    val stats = new TestStatistics
    var last = myPolicy2.decide(stats)
    println(last)
    stats.totalRunCount = 500
    last = last._2.decide(stats)
    println(last)
    stats.totalRunCount = 1000
    last = last._2.decide(stats)
    println(last)
    last = last._2.decide(stats)
    println(last)
    stats.totalRunCount = 200
    last = last._2.decide(stats)
    println(last)
  }

  def test2(): Unit = {
    val stats = new TestStatistics
    stats.totalRunCount = 5000
    var last = conditional.decide(stats)
    println(last) // Expected gatherData
    stats.totalRunCount = 5030
    last = last._2.decide(stats)
    println(last) // Expected gatherData
    stats.totalRunCount = 5051
    last = last._2.decide(stats)
    println(last) // Expected selectNew
    stats.totalRunCount = 5055
    last = last._2.decide(stats)
    println(last) // Expected selectNew
    stats.totalRunCount = 5200
    last = last._2.decide(stats)
    println(last) // Expected gatherData
    stats.totalRunCount = 5210
    last = last._2.decide(stats)
    println(last) // Expected gatherData
    stats.totalRunCount = 5500
    stats.streakLength = 50
    last = last._2.decide(stats)
    println(last) // Expected useLast
    last = last._2.decide(stats)
    println(last) // Expected useLast
    last = last._2.decide(stats)
    println(last) // Expected useLast
  }

  def test3(): Unit = {
    val stats = new TestStatistics
    var last = checking.decide(stats)
    println(last) // Expected selectNew
    last = last._2.decide(stats)
    println(last) // Expected selectNew
    stats.streakLength = 50
    last = last._2.decide(stats)
    println(last) // Expected useLast
    last = last._2.decide(stats)
    println(last) // Expected useLast
    last = last._2.decide(stats)
    println(last) // Expected useLast

    last = checking.decide(stats)
    println(last) // Expected selectNew
    last = last._2.decide(stats)
    println(last) // Expected useLast

    stats.streakLength = 0
    last = checking.decide(stats)
    println(last) // Expected selectNew
    last = last._2.decide(stats)
    println(last) // Expected selectNew

    stats.mostRunCount = 100
    stats.totalRunCount = 100
    last = last._2.decide(stats)
    println(last) // Expected useMost
    last = last._2.decide(stats)
    println(last) // Expected useMost

    stats.streakLength = 50
    last = checking.decide(stats)
    println(last) // Expected selectNew
    last = last._2.decide(stats)
    println(last) // Expected useLast
  }

  def testAndPolicy(): Unit = {
    val stats = new TestStatistics
    stats.mostRunCount = 5
    stats.totalRunCount = 10
    var last = andPolicy.decide(stats)
    println(last) // Expected selectNew

    stats.mostRunCount = 100
    stats.totalRunCount = 200

    last = last._2.decide(stats)
    println(last) // Expected selectNew

    stats.mostRunCount = 200
    stats.totalRunCount = 300

    last = last._2.decide(stats)
    println(last) // Might be useMost

    while (last._1 == PolicyResult.UseMost) {
      last = last._2.decide(stats)
      println(last) // Expected useMost
    }

    last = last._2.decide(stats)
    println(last) // Expected selectNew
  }

  def main(args: Array[String]): Unit = {
    testAndPolicy()
  }
}
