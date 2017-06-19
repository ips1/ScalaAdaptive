package scalaadaptive.api.grouping

/**
  * Created by pk250187 on 3/20/17.
  */
class LogarithmGroupSelector extends GroupSelector {
  override def selectGroupForValue(value: Long): GroupId = Group(scala.math.log10(value).toInt)
}