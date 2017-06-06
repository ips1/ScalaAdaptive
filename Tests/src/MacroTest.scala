import adaptivetests.sorting.Sorter

import scalaadaptive.core.functions.adaptors.FunctionAdaptor1

/**
  * Created by pk250187 on 4/9/17.
  */
object MacroTest {
  import scalaadaptive.core.macros.MacroUtils._
  import scalaadaptive.api.Implicits._

  def increment(i: Int): Int = i + 1
  def increment2(i: Int): Int = {
    val value = i + 1
    value
  }
  def incrementSlow(i: Int): Int = {
    Thread.sleep(10)
    i + 1
  }

  val x = 15
  //printAst(x + 1)

  def getSorter(): Sorter = new Sorter()

  val sort = new Sorter()
  //val test123 = printAst(increment _)

//  val testXXXX = printAst(sort.selectionSort(List()))

//  val testXXX = printAst(classOf[Sorter].getTypeName + ".ahoj")

//  val test = printAst(scalaadaptive.api.Implicits.toAdaptor({
//    (i: Int) => MacroTest.this.increment(i)
//  }, scalaadaptive.core.functions.references.MethodNameReference.apply("MacroTest.this.increment")))

  val incFun1 = incrementSlow _ or increment
  val incFun2 = incrementSlow _ or increment
  val incFun3 = incrementSlow _ or increment2

  def main(args: Array[String]): Unit = {
    List(incFun1, incFun2, incFun3).foreach(fun => Seq.range(0, 50).foreach(i => println(fun(i))))
  }
}
