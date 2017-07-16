/**
  * Created by Petr Kubat on 4/9/17.
  */
object AdaptorDefinitions {
  import scalaadaptive.api.Implicits._
  val fun1 = toAdaptiveFunction1 { list: List[Int] =>
    list.sorted
  }


}
