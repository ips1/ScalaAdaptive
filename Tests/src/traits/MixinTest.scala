package traits

/**
  * Created by Petr Kubat on 7/17/17.
  */
class MixinTest {

  class Parent
  trait TraitA extends Parent
  trait TraitB extends Parent
  class Child extends TraitA with TraitB

  trait IntSequence {
    val data: Seq[Int]
  }

  class IntList(val data: List[Int]) extends IntSequence

  trait SequenceWithAverage extends IntSequence {
    def average(): Double = data.sum / data.size
  }

  val listWithAverage = new IntList(List(1, 5, 200)) with SequenceWithAverage
  val average = listWithAverage.average()

}
