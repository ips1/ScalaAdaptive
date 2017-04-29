import adaptivetests.sorting.Sorter

import scala.util.Random

/**
  * Created by pk250187 on 3/19/17.
  */
object Test {
  def main(args: Array[String]): Unit = {
    import Sortable._

    val runner = new TestRunner()

    //runTest(l => l.sort())
    val sorter = new Sorter()
    //runTest(l => sorter.sort(l))
    Seq.range(0, 1).foreach(i => {
      runner.runIncrementalTest(l => sorter.sort(l))
    })
  }
}
