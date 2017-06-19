package scalaadaptive.api.grouping

/**
  * Created by pk250187 on 4/23/17.
  */
class SingleGroupSelector extends GroupSelector {
  override def selectGroupForValue(value: Long): GroupId = defaultGroup
}
