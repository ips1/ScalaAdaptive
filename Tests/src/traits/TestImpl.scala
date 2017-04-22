package traits

/**
  * Created by pk250187 on 4/5/17.
  */
class TestImpl extends TestTrait {
  import functionadaptors.Implicits._

  def impl1(arg: List[Int]): List[String] = arg.map("Num: " + _.toString)
  def impl2(arg: List[Int]): List[String] = arg.map(i => s"Num: $i")

  override val testFunction: (List[Int]) => List[String] = impl1 _ or impl2

  // Can't implement testMethod using the result of or()
  override def testMethod(arg: List[Int]): List[String] = ???
}

class NewTestImple extends TestTrait {
  import functionadaptors.Implicits._

  def impl1(arg: List[Int]): List[String] = arg.map("Num: " + _.toString)
  def impl2(arg: List[Int]): List[String] = arg.map(i => s"Num: $i")

  private val adaptedFunction = impl1 _ or impl2
  override def testMethod(arg: List[Int]): List[String] = (impl1 _ or impl2)(arg)

  override val testFunction: (List[Int]) => List[String] = null
}
