import scalaadaptive.api.options.Storage

/**
  * Created by Petr Kubat on 4/29/17.
  */
object WrongUse {
  import scalaadaptive.api.Implicits._
  def impl1(data: List[Int]): Int = ???
  def impl2(data: List[Int]): Int = ???

  def processData(data: List[Int]): Int = (impl1 _ or impl2 storeUsing Storage.Local)(data)
}
