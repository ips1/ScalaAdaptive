package traits

/**
  * Created by Petr Kubat on 4/5/17.
  */
trait TestTrait {
  def testMethod(arg: List[Int]): List[String]
  val testFunction: (List[Int]) => List[String]
}
