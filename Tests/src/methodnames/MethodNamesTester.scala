/**
  * Created by pk250187 on 4/30/17.
  */
package methodnames

import adaptivetests.sorting.Sorter

object MethodNamesTester {
  import scalaadaptive.api.Implicits._

  def method1(i: Int): Int = ???
  def method2(i: Int): Int = ???

  val fun1: (Int) => Int = (i: Int) => ???
  val fun2: (Int) => Int = (i: Int) => ???

  val sorter = new Sorter()

  var variableSorter = new Sorter()

  def getSorter: Sorter = new Sorter()

  val thisCall = method1 _ or method2
  val valCall = sorter.standardSort _ or sorter.selectionSort
  val varCall = variableSorter.standardSort _ or variableSorter.selectionSort
  val etaLikeLambdaCall = ((i: Int) => method1(i)) or method2
  val lambdaCalls = ((i: Int) => method1(i + 1)) or method2
  val lambdaCalls2 = ((i: Int) => { val x = i * 2; x + 1 }) or method2
  val methodCall = getSorter.standardSort _ or getSorter.selectionSort
  val ctorCall = new Sorter().standardSort _ or new Sorter().selectionSort
  val funCall = fun1 or fun2

  def main(args: Array[String]): Unit = {
    println(thisCall.toDebugString)
    println(valCall.toDebugString)
    println(varCall.toDebugString)
    println(etaLikeLambdaCall.toDebugString)
    println(lambdaCalls.toDebugString)
    println(lambdaCalls2.toDebugString)
    println(methodCall.toDebugString)
    println(ctorCall.toDebugString)
    println(funCall.toDebugString)
  }
}
