package adaptivetests.testmethods

import scalaadaptive.core.options.{Selection, Storage}

/**
  * Created by pk250187 on 5/1/17.
  */
class TestMethods {
  import scalaadaptive.api.Implicits._

  val slowSleepTime = 100
  val fastSleepTime = 10

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

  def slowMethod(x: List[Int]): List[Int] = {
    Thread.sleep(100)
    x
  }

  def fastMethod(x: List[Int]): List[Int] = {
    Thread.sleep(10)
    x
  }

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

  val function = (
    linearHighConstant _ or quadraticMinConstant
    by (x => x.size)
    selectUsing Selection.Discrete
    storeUsing Storage.Persistent
  )
}
