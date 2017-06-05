package adaptivetests.manyargs

import scala.util.Random

/**
  * Created by pk250187 on 6/5/17.
  */
class DataGenerator {
  private def nextString(rand: Random, length: Int): String =
    List.fill(length)(rand.alphanumeric.filter(_.isLetter).head).toString()

  def generateData(size: Int): Seq[(Int, String)] = {
    val random = new Random(42)
    Seq.range(0, size).map(i => (random.nextInt(size), nextString(random, random.nextInt(20) + 1)))
  }
}
