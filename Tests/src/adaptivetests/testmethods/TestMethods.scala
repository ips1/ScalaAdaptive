package adaptivetests.testmethods

import scalaadaptive.api.options.{Selection, Storage}
import scalaadaptive.core.functions.policies.{LimitedOverheadPolicy, StopSelectingWhenDecidedPolicy}

/**
  * Created by pk250187 on 5/1/17.
  */
class TestMethods {
  import scalaadaptive.api.Implicits._

  def sleepNanos(nanos: Long): Unit = {
    val milis = nanos / 1000000
    val remainingNanos = (nanos - (milis * 1000000)).toInt
    Thread.sleep(milis, remainingNanos)
  }

  def iterate(times: Int): Long =
    Seq
      .range(0, times)
      .map(i => Seq.range(0, 20).sum + i)
      .sum

  val slowSleepTime = 100
  val fastSleepTime = 10

  val highConstant = 100
  val minConstant = 1
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
    println("slow")
    Thread.sleep(20)
    x
  }

  def fastMethod(x: List[Int]): List[Int] = {
    println("fast")
    Thread.sleep(10)
    x
  }

  def anotherFastMethod(x: List[Int]): List[Int] = {
    println("fast")
    Thread.sleep(10)
    x
  }

  def slowestMethod(x: List[Int]): List[Int] = {
    println("slowert")
    Thread.sleep(30)
    x
  }

  def anotherSlowMethod(x: List[Int]): List[Int] = {
    println("slow")
    Thread.sleep(20)
    x
  }

  def linearMinConstant(x: List[Int]): List[Int] = {
    //println("linearMinConstant")
    val sleepTime = ((x.size * minConstant + highConstant * highConstant)).toInt
    //sleepNanos(sleepTime)
    Thread.sleep(sleepTime)
//    iterate(sleepTime)
    x
  }

  def linearHighConstant(x: List[Int]): List[Int] = {
    //println("linearHighConstant")
    val sleepTime = ((x.size * highConstant)).toInt
    //sleepNanos(sleepTime)
    Thread.sleep(sleepTime)
//    iterate(sleepTime)
    x
  }

  def quadraticMinConstant(x: List[Int]): List[Int] = {
    //println("quadraticMinConstant")
    val sleepTime = ((x.size * x.size * minConstant)).toInt
    //sleepNanos(sleepTime)
    Thread.sleep(sleepTime)
//    iterate(sleepTime)
    x
  }

  val functionDiscrete = (
    linearHighConstant _ or quadraticMinConstant
    by (x => x.size)
    selectUsing Selection.Discrete
    storeUsing Storage.Persistent
  )

  val functionContinuous = (
    linearHighConstant _ or quadraticMinConstant
      by (x => x.size)
      selectUsing Selection.Continuous
      storeUsing Storage.Persistent
      //withPolicy new StopSelectingWhenDecidedPolicy(80, 0.6)
    )

  val linearFunctionsContinuous = (
    linearHighConstant _ or linearMinConstant
      by (x => x.size)
      selectUsing Selection.Continuous
      storeUsing Storage.Persistent
    //withPolicy new StopSelectingWhenDecidedPolicy(80, 0.6)
    )

  val functionContinuousWithLimitedOverhead =
    functionContinuous withPolicy new LimitedOverheadPolicy(1000 * 1000 * 1000, 1000 * 1000 * 200)

  val function = functionDiscrete
}
