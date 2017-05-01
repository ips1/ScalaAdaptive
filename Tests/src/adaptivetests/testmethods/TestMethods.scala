package adaptivetests.testmethods

import scalaadaptive.core.options.Storage

/**
  * Created by pk250187 on 5/1/17.
  */
class TestMethods {
  import scalaadaptive.api.Implicits._

  val highConstant = 20
  val minConstant = 0.5
//
//  def linearHighConstant(x: Int): Int = {
//    val sleepTime = ((x * highConstant) / 10).toInt
//    Thread.sleep(sleepTime)
//    (math.random * 100).toInt
//  }
//
//  def quadraticMinConstant(x: Int): Int = {
//    val sleepTime = ((x * x * minConstant) / 10).toInt
//    Thread.sleep(sleepTime)
//    (math.random * 100).toInt
//  }

  def linearHighConstant(x: List[Int]): List[Int] = {
    val sleepTime = ((x.size * highConstant) / 100).toInt
    Thread.sleep(sleepTime)
    x
  }

  def quadraticMinConstant(x: List[Int]): List[Int] = {
    val sleepTime = ((x.size * x.size * minConstant) / 100).toInt
    Thread.sleep(sleepTime)
    x
  }


  val function = linearHighConstant _ or quadraticMinConstant by (x => x.size) withStorage Storage.Persistent
}
