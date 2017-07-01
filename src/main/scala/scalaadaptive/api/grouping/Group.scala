package scalaadaptive.api.grouping

/**
  * Created by pk250187 on 3/21/17.
  */
abstract class Group

case class GroupId(id: Int) extends Group
case class NoGroup() extends Group