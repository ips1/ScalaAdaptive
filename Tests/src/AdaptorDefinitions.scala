/**
  * Created by pk250187 on 4/9/17.
  */
object AdaptorDefinitions {
  import scalaadaptive.api.Implicits._
  val fun1 = toAdaptor { list: List[Int] =>
    list.sorted
  }


}
