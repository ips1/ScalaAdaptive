package scalaadaptive.core.runtime.selection

import org.apache.commons.math3.stat.descriptive.{DescriptiveStatistics, SummaryStatistics}
import org.apache.commons.math3.stat.inference.TestUtils

import scalaadaptive.core.runtime.history.runhistory.RunHistory
import scalaadaptive.math.{HigherExpectation, LowerExpectation, TTestRunner}

/**
  * Created by pk250187 on 5/2/17.
  */
class TTestSelector(val secondarySelector: RunSelector[Long],
                    val alpha: Double) extends RunSelector[Long] {
  val testRunner = new TTestRunner(() => logger)

  override def selectOption(records: Seq[RunHistory[Long]], inputDescriptor: Option[Long]): RunHistory[Long] = {
    logger.log("Selecting using TTestSelector")

    val statistics = records.map(rh => (rh, rh.runStatistics))

    var leader = statistics.head

    statistics.tail.foreach(item => {
      val testResult = testRunner.runTest(leader._2, item._2, alpha)

      testResult match {
        case Some(HigherExpectation()) => leader = item
        case Some(LowerExpectation()) =>
        case _ => return secondarySelector.selectOption(List(leader._1, item._1), inputDescriptor)
      }
    })

    leader._1
  }
}
