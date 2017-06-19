package scalaadaptive.api.grouping

/**
  * Created by pk250187 on 3/20/17.
  */
trait GroupSelector {
  val defaultGroup = NoGroup()
  def selectGroupForValue(value: Long): GroupId
}
