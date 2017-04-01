package grouping

/**
  * Created by pk250187 on 3/20/17.
  */
trait GroupSelector {
  val defaultGroup = new GroupId(0)
  def selectGroupForValue(value: Int): GroupId
}
