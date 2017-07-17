package adaptivetests.overhead

import adaptivetests.sorting.Sorter

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.defaults.DefaultConfiguration
import scalaadaptive.api.policies.StopSelectingWhenDecidedPolicy
import scalaadaptive.core.configuration.blocks.logging.NoLogging
import scalaadaptive.core.configuration.blocks.storage.CachedStatisticsStorage

/**
  * Created by Petr Kubat on 6/5/17.
  */
object SortingOverhead {
  def customData(size: Int): List[Int] = Seq.fill(size)(Random.nextInt).toList

  val sorter = new Sorter()

  val utils = new OverheadUtils()

  def main(args: Array[String]): Unit = {
    Adaptive.initialize(new DefaultConfiguration with CachedStatisticsStorage)

    val dataSize = 1000
    val data = customData(1000)

    import scalaadaptive.api.Implicits._
    val customSort = sorter.standardSort _ or sorter.selectionSort withPolicy new StopSelectingWhenDecidedPolicy(20)

    val runCount = 1000

    utils.performTest(runCount, data, sorter.standardSort, customSort)
  }
}
