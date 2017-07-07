package adaptivetests.strategycomparison

import scala.util.Random

/**
  * Created by pk250187 on 6/24/17.
  */
class Methods {
  def createSlower(by: Double): (Int) => Long =
    i => {
      val j = (i * (1 + by)).toInt
      var acc = 0
      Seq.range(0, j).foreach(k => {
        acc = acc + (k * Random.nextInt(1000))
      })
      acc
    }

  def createNormal: (Int) => Long =
    i => {
      var acc = 0
      Seq.range(0, i).foreach(k => {
        acc = acc + (k * Random.nextInt(1000))
      })
      acc
    }
}
