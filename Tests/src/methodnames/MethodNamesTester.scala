/**
  * Created by pk250187 on 4/30/17.
  */
package methodnames

import adaptivetests.sorting.Sorter

object MethodNamesTester {
  import scalaadaptive.api.Implicits._
  import scalaadaptive.core.macros.MacroUtils._
  import scala.math._

  def method1(i: Int): Int = ???
  def method2(i: Int): Int = ???

  def overloadedMethod(i: Int): Int = ???
  def overloadedMethod(i: String): Int = ???
  def overloadedMethod(i: Int, j: String): Int = ???

  def genericMethod[T](t: T): T = ???

  def genericMethodMultipleArgs[T, U](t: T): U = ???

  def genericMethodImplicit[T](t: T)(implicit ord: T => Ordered[T]): T = ???

  def genericMethodImplicitMultipleArgs[T, U](t: T, u: U)(implicit ord: T => Ordered[T], num: U => Ordered[U]): T = ???

  def genericMethodList[T](t: List[T]): T = ???

  val fun1: (Int) => Int = (i: Int) => ???
  val fun2: (Int) => Int = (i: Int) => ???

  val sorter = new Sorter()

  var variableSorter = new Sorter()

  def getSorter: Sorter = new Sorter()

//  def getGenericMethod[T] = printAst(genericMethod _)
//  def getGenericMethod2[T] = printAst(genericMethodList[T] _)
//  def getGenericMethod3[T](implicit ord: T => Ordered[T]) = printAst(sorter.bubbleSort[T] _)

  val oveloadResolution: (Int) => Int = overloadedMethod

  printAst(genericMethod[Int] _)
  printAst(genericMethodMultipleArgs[Int, Double] _)
  printAst(genericMethodImplicit[Int] _)
  printAst(genericMethodImplicitMultipleArgs[Double, Int] _)

  val thisCall = method1 _ or method2
  val genericThisCall = genericMethod[Int] _ or method1
  val valCall = sorter.standardSort _ or sorter.selectionSort
  val varCall = variableSorter.standardSort _ or variableSorter.selectionSort
  val etaLikeLambdaCall = ((i: Int) => method1(i)) or method2
  val lambdaCalls = ((i: Int) => method1(i + 1)) or method2
  val lambdaCalls2 = ((i: Int) => { val x = i * 2; x + 1 }) or method2
  val methodCall = getSorter.standardSort _ or getSorter.selectionSort
  val ctorCall = new Sorter().standardSort _ or new Sorter().selectionSort
  val genericCtorCall = new GenericClass[Int]().doStuff _ or method2
  val funCall = fun1 or fun2

  def main(args: Array[String]): Unit = {
    println(thisCall.toDebugString)
    println(genericThisCall.toDebugString)
    println(valCall.toDebugString)
    println(varCall.toDebugString)
    println(etaLikeLambdaCall.toDebugString)
    println(lambdaCalls.toDebugString)
    println(lambdaCalls2.toDebugString)
    println(methodCall.toDebugString)
    println(ctorCall.toDebugString)
    println(genericCtorCall.toDebugString)
    println(funCall.toDebugString)
  }
}
